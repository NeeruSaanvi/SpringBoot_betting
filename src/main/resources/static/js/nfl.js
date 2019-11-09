//functionality done but data not getting for this

  $(document).ready(function (e) {
	
  $("#data-toogle-2").hide();
	 
	$("#data-toogle-1").click(function () {
	  $("#expand").text("Expand for game details");
       $("#data-toogle-2").show();
	   $(".table_show").hide();
	  
	   $(this).hide();

    });
	
	$('#data-toogle-2').click(function(){
   ///$("#table_show").show();
   $("#expand").text("Collapse for game details");
   $("#data-toogle-1").show();
   $(".table_show").show();
   $(this).hide();
    //
	//$('span').hide()
	//$('#data-toogle-2').show();
    });
	
    $("#part_1").hide();
    $("#btn_1").click(function () {

      $("#part_1").show(1000);
      $("#part_2").hide(1000);
    });

    $("#btn_2").click(function () {
      $("#part_2").show(1000);
      $("#part_1").hide(1000);
    });
  });

  $(document).ready(function (e) {
    $("#tab_1").hide();
    $("#btn_3").click(function () {

      $("#tab_2").show(1000);
      $("#tab_1").hide(1000);
    });

    $("#btn_4").click(function () {
      $("#tab_1").show(1000);
      $("#tab_2").hide(1000);
    });
  });

 $(document).ready(function(){

  $("#part_1").hide();
  $("#btn_1").click(function(){
    $("#part_2").hide();
    $("#part_1  ").show("slide",{direction: 'right'}, 1000);
  });
  $("#btn_2").click(function(){
    $("#part_1").hide();
    $("#part_2").show("slide",{direction: 'left'}, 1000);
  });


  });
 
 $(document).ready(function(){
	  var sportName = sessionStorage.getItem('sport_name');
	  var teamName = sessionStorage.getItem('team_name');
	  if(!(sportName!=undefined && teamName!=undefined && teamName.length>0 && sportName.length>0 && teamName.length>0)){
		  sportName='nfl';
		  teamName='Chicago Bulls';
	  }
	  $("#teamNameId").text(teamName);
	  sessionStorage.setItem('sport','nfl');
	  initializeUI(sportName, teamName);
	  sessionStorage.removeItem('sport_name');
	  sessionStorage.removeItem('team_name');
 });


function initializeUI(sport,teamName){
	var url = "/playersinfo/info/v1/teamplayers?team="+teamName+"&sport="+sport+"&game=game&line=spread&sportsbook=BOOKMAKER LINE MOVEMENTS";
	$.fn.requestCall('GET','',url,80000,function(data){
		console.log(data);
		
		var obj = JSON.parse(data);
		var resObj = JSON.parse(data);
		playerTeam = resObj.playersData;
		topTblData = resObj.topTableData;
		headerMap = resObj.headerRow;
		setTopTableRecord(topTblData);
		setPlayersRecord(playerTeam);
		setPlayerRecord1(playerTeam);
	});
	
		function setTopTableRecord(topTblData) {
			var tableStr='<table class="a table table-borderless mb-0">';
			var tableRowStr="";
			var avrgJson={};
			var keySet = [ "week", "opponent", "points",
			      		   "oppPoints","offensiveYPP","defensiveYPP","closingSpread","atsResult","sideCoverMargin", 
			      		   "closingTotal","overUnder","totalCoverMargin"];
			
			var showKey=[ "Week", "Opponent", "Points", "Opp Points","Offensive YPP", 
						  "Defensive YPP", "Closing Spread", "ATS Results","Side Cover Margin",
						  "Closing Total","O/U Result","Total Cover Margin"];
			
			var avrgKeySet=[ "Points", "Opp Points","Offensive YPP", 
				  			"Defensive YPP", "Closing Spread", "ATS Results","Side Cover Margin",
				  			"Closing Total","O/U Result","Total Cover Margin"]; 
			
			for(var i=0; i<showKey.length; i++) {
				rowStr = createTableRow(topTblData,keySet[i],showKey[i],avrgJson,isExistsAvrgKey(avrgKeySet,showKey[i]));
				tableRowStr+=rowStr;
			}
			$("#part_2").html(tableStr+tableRowStr+"</table>");
			//console.log(avrgJson);
			showAvrageRecord(avrgKeySet, avrgJson);
		}
		
		function createTableRow(topTableData, key,showKey,avrgJson,isExistsAvrgKey) {
			var rowCols="<td>"+showKey+"</td>";
			var avrg=0;
			var countL=0;  //used for atsResult and overUnder Keys 
			var countPush=0; //used for atsResult
			var overcount=0;
			var undercount=0;
			for(var headerKeyName in headerMap) {
				var headKeyValue = headerMap[headerKeyName];
				var index=isExistsHeaders(headerKeyName,topTableData);
				if(index!=-1) {
					var record = topTableData[index][key];
					if(record!=null && isExistsAvrgKey && !isNaN(record)) {
						avrg+=parseInt(record);
						//console.log(key)
					}else{
						if(key==="overUnder" && record==="O"){
							overcount++;
						}else{
							undercount++;
						}
					}
					record = record!=null ? record : "-";
					
					if(key=='date'&&record.length==10) {
						record=record.substring(5);
					}
					
					if(key=='atsResult') {
						if(record=='L'||record=='l'){
							countL++;
						}else if(record=='push'||record=='Push'||record=='PUSH'){
							countPush++;
						}
					}
					
					rowCols+="<td>"+record+"</td>";
				}
			}
			if(isExistsAvrgKey){
				if(key==="overUnder"){
					avrgJson[showKey]=overcount+"-"+undercount;
				}else{
					avrgJson[showKey]=(avrg/topTableData.length).toFixed(1);
				}
			}
			
			console.log("winRslt "+ (topTableData.length-countPush)-countL);
			if(isExistsAvrgKey && key=='atsResult') {
				var winRslt = (topTableData.length-countPush)-countL;
				avrgJson[showKey]=winRslt+"-"+countL;
			}
			
			return "<tr class='a'>"+rowCols+"</tr>";
		}
		
		function isExistsHeaders(headerKeyDate,topTableData) {
			var index=-1;
			for(var i=0; i<topTableData.length; i++) {
				var date = topTableData[i]['date'];
				var result = topTableData[i]['result'];
				var headerKeyResult = headerMap[headerKeyDate];
				if(date!=null && result !=null && date==headerKeyDate && result==headerKeyResult){
					index=i
					break;
				}
			}
			return index;
		}
		
		function isExistsAvrgKey(avrgKeySet,key) {
			var status=false;
			for(var i=0;i<avrgKeySet.length;i++) {
				if(avrgKeySet[i]==key) {
					status=true; break;
				}
			}
			return status;
		}
		
		function showAvrageRecord(showKey, avrgJson) {
			//var tableHeads="<tr><th>All Games</th><th>Average</th></tr>";
			var rowsCols="";
			for(var i=0;i<showKey.length;i++) {
				//console.log(showKey[i]+", "+avrgJson[showKey[i]])
				rowsCols+="<tr><td>"+showKey[i]+"</td><td>"+(avrgJson[showKey[i]])+"</td></tr>";
			}
			//$("#gameAvrgId thead").html(tableHeads);
			//$("#gameAvrgId tbody").html(rowsCols);
			//console.log(rowsCols)
			$("#averagetr").html(rowsCols);
		}
	
		function setPlayersRecord(obj) {
			var trs = "";
			var ths ="";
			var rowno=1;
			 $("#myTable tbody").html('');
			 
			 ths +=" <tr class='a dark-blue c-white'>";
			 ths +="<th colspan='2'>Player</th>";
			 ths+="<th >Pos</th>";
	         
			 ths+=" <th>Total Snaps</th>";
			 ths+="<th>Avg. Snaps</th>";
			
			 var dateArray = [];
			 var thArrray = [];
			 for ( var k in obj) {

							if (obj[k] instanceof Object) {
								if (obj[k].games != null) {
									getDateArray(obj[k].games, dateArray);
								}
							}

						}
			
			var uniqueArray = createDateArray();
			
			 
			 var thead ="<thead>";
			 thead +="<tr class='a bg-dark-blue c-balck'> <th colspan='5' class='c-white'>OFFENSIVE SNAPS</th>";
			 uniqueArray.sort(function(a,b){
				  return +new Date(a.gameDate) - +new Date(b.gameDate);
				});
	         
			 
				$.each(uniqueArray, function(i, obj1){
					 thead +="<th>";
					thead+=obj1.week;
					thead+="</th>"
						ths +="<th>";
				      ths +=obj1.opponent +"<br>"+ obj1.result;
				      ths +="</th>"
				 }); 
			 thead+="</tr> </thead>";
			 ths +="</tr>";
			 
			 for(var k in obj) {
				 var id = k.replace(/\s/g, "");
				 
				 var createrow = false;
				 $.each(obj[k].games, function(ind, gobj){
					if(Number(gobj.offensive_snap)!=0)
					 createrow = true;
				 });
				 
				 if(createrow){
				 if(obj[k] instanceof Object) {
					 trs += "<tr>";
					 trs += "<td class='a'>"+rowno+"</td>";
					 trs += "<td class='a'>"+obj[k].playerName+"</td>";
					 trs += "<td class='a'>"+obj[k].position+"</td>";
					 
					 //average code write here
					 //logic
					 var averageSnap=0;
					 var totalSnap=0;
					 var count = 0;
					 $.each(obj[k].games, function(ind, gobj){
						 //console.log("Con "+JSON.stringify(gobj));
						 averageSnap += Number(gobj.offensive_snap);
						 totalSnap += Number(gobj.offensive_snap);
						 count++;
					 });
					 
					 averageSnap = (averageSnap/count).toFixed(2);
					 trs += "<td class='a'>"+totalSnap.toFixed(2)+"</td>";
					 
						if(obj[k].games!=null) {
							trs += "<td class='a'>"+averageSnap+"</td>";
						}else {
							trs += "<td class='a'></td>";
						}
						
						for (var i = 0; i < uniqueArray.length; i++) {
								var obj1 = uniqueArray[i];
							
							if(obj[k].games!=null){
								for (var j = 0; j < obj[k].games.length; j++) {
									
								    var innerObject = obj[k].games[j];
								   
								    var offensiveSnap = Number(innerObject.offensive_snap);
									    if( offensiveSnap!=0) {
									    	//console.log(innerObject.week+", "+offensiveSnap);
											if(obj1.week === innerObject.week){
												var minute = Number(innerObject.offensive_snap);
												if(minute!=null ) {
												    	trs += "<td class='a'>"+Math.round(minute)+"</td>";
												    	 break;
												 }
											} 
									    } else {
									    	if(obj1.week === innerObject.week){
									    		if(averageSnap>=20 && averageSnap<25){
													//console.log("greater 20: "+trs);
													trs += "<td class='a' style='background:yellow; border:none'></td>";
												} else if(averageSnap>=25 && averageSnap<30){
													trs += "<td class='a' style='background:#dda702; border:none'></td>";
												} else if(averageSnap>=30){
													trs += "<td class='a' style='background:red; border:none'></td>";
												} else
													trs += "<td class='a'></td>";
									    		
									    	//	trs += "<td class='a'></td>";
									    	}
								    }// if offensive snaps are non zero
								    
								}//iterate games 
								
							}// check if game is not null
							
						}//);
					 
					 
					 trs +="</tr>";
					 rowno++;
				 }
			 }

				 if(rowno==dateArray.length){
					 break;
				 }
				
			    }
			 $("#myTable tbody").html(thead+ths+trs);
		}
		
	
	function setPlayerRecord1(obj){

		var trs = "";
		var ths ="";
		var rowno=1;
		 $("#myTable1 tbody").html('');
		 
		 ths +=" <tr class='a dark-blue c-white'>";
		 ths +="<th colspan='2'>Player</th>";
		 ths+="<th >Pos</th>";
         
		 ths+=" <th>Total Snaps</th>";
		 ths+="<th>Avg. Snaps</th>";
		
		 var dateArray = [];
		 var thArrray = [];
		 for ( var k in obj) {

						if (obj[k] instanceof Object) {
							if (obj[k].games != null) {
								getDateArray(obj[k].games, dateArray);
							}
						}

					}
		 
		 //ddmmyyyyFormatArray = [];
		 
		
		//var uniqueArray = removeDuplicates(dateArray, "gameDate");
		var uniqueArray = createDateArray();
		 
		 var thead ="<thead>";
		 thead +="<tr class='a bg-dark-blue c-balck'> <th colspan='5' class='c-white'>DEFFENSIVE SNAPS</th>";
		 uniqueArray.sort(function(a,b){
			  return +new Date(a.gameDate) - +new Date(b.gameDate);
			});
         
			$.each(uniqueArray, function(i, obj1){
				 thead +="<th>";
				thead+=obj1.week;
				thead+="</th>"
					ths +="<th>";
			      ths +=obj1.opponent +"<br>"+ obj1.result;
			      ths +="</th>"
			 }); 
		 thead+="</tr> </thead>";
		 ths +="</tr>";
		 
		 for(var k in obj) {
			 var id = k.replace(/\s/g, "");
			 
			 var createrow = false;
			 $.each(obj[k].games, function(ind, gobj){
				if(Number(gobj.defensive_snap)!=0)
				 createrow = true;
			 });
			 
			 if(createrow){
			 if(obj[k] instanceof Object) {
				 trs += "<tr>";
				 trs += "<td class='a'>"+rowno+"</td>";
				 trs += "<td class='a'>"+obj[k].playerName+"</td>";
				 trs += "<td class='a'>"+obj[k].position+"</td>";
				 
				 //average code write here
				 //logic
				 var averageSnap=0;
				 var totalSnap=0;
				 var count = 0;
				 $.each(obj[k].games, function(ind, gobj){
					 //console.log("Con "+JSON.stringify(gobj));
					 averageSnap += Number(gobj.defensive_snap);
					 totalSnap += Number(gobj.defensive_snap);
					 count++;
				 });
				 
				 averageSnap = (averageSnap/count).toFixed(2);
				 trs += "<td class='a'>"+totalSnap.toFixed(2)+"</td>";
				 
					if(obj[k].games!=null) {
						trs += "<td class='a'>"+averageSnap+"</td>";
					}else {
						trs += "<td class='a'></td>";
					}
					
					for (var i = 0; i < uniqueArray.length; i++) {
							var obj1 = uniqueArray[i];
						
						if(obj[k].games!=null){
							for (var j = 0; j < obj[k].games.length; j++) {
								
							    var innerObject = obj[k].games[j];
							   
							    var defensiveSnap = Number(innerObject.defensive_snap);
								    if( defensiveSnap != 0) {
								    	//console.log(innerObject.week+", "+offensiveSnap);
										if(obj1.week === innerObject.week){
											var minute = Number(innerObject.defensive_snap);
											if(minute!=null ){
											    	trs += "<td class='a'>"+Math.round(minute)+"</td>";
											    	 break;
											 }
										} 
								    } else {
									    	if(obj1.week === innerObject.week){
									    		if(averageSnap>=20 && averageSnap<25){
													//console.log("greater 20: "+trs);
													trs += "<td class='a' style='background:yellow; border:none'></td>";
												} else if(averageSnap>=25 && averageSnap<30){
													trs += "<td class='a' style='background:#dda702; border:none'></td>";
												} else if(averageSnap>=30){
													trs += "<td class='a' style='background:red; border:none'></td>";
												} else
													trs += "<td class='a'></td>";
									    	}
								    }// if defensive snaps are non zero and zero
							    
							}//iterate games 
							
						}// check if game is not null
						
					}//);
				 
				 
				 trs +="</tr>";
				 rowno++;
			 }
			 }
			 if(rowno==dateArray.length){
				 break;
			 }
			 
		    }
		 $("#myTable1 tbody").html(thead+ths+trs);
	
	}
	function getDateArray(arrayOfObj,array){
		var count = 1;
		$.each(arrayOfObj, function(i, obj){
			var obje = {};
			obje.gameDate = obj.gameDate;
			obje.opponent = obj.opponent;
			obje.result = obj.result;
			if (!array.includes(obje)) {
				var object = {};
				object.gameDate = obj.gameDate;
				object.opponent = obj.opponent;
				object.result = obj.result;
				    array.push(object);
			}
		 }); 		
	}
	
	
	
	function createDateArray(playerTeam){
		var dateArr=[];
		for(var dateKey in headerMap) {
			var jsnObj={};
			jsnObj['gameDate']=dateKey;
			jsnObj['result']=headerMap[dateKey];
			jsnObj['opponent']=getOpponent(dateKey, headerMap[dateKey]);
			jsnObj['week']=getWeek(dateKey,headerMap[dateKey]);
			dateArr.push(jsnObj);
		}
		return dateArr;
	}
	
	function getOpponent(dateKey,result){
		var opponent="-";
		for(var i=0;i<topTblData.length;i++){
			var obj=topTblData[i];
			if(obj['date']==dateKey && result==obj['result']){
				opponent=topTblData[i]['opponent'];
				if(opponent==null) 
					opponent="-";
				break;
			}
		}
		return opponent;
	}
	
	function getWeek(dateKey,result){
		var week="-";
		for(var i=0;i<topTblData.length;i++){
			var obj=topTblData[i];
			if(obj['date']==dateKey && result==obj['result']){
				week=topTblData[i]['week'];
				if(week==null) 
					week="-";
				break;
			}
		}
		return week;
	}
	
	function changeDateFormat(inputDate){  
		//console.log(inputDate)
	    var splitDate = inputDate.split('-');
	    if(splitDate.count == 0){
	        return null;
	    }

	    var year = splitDate[0];
	    var month = splitDate[1];
	    var day = splitDate[2]; 

	    return month + '-' + day ;
	}
	
}
