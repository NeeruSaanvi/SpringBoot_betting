
  $(document).ready(function (e) {
	  
	  
  $("#data-toogle-2").hide();
	 
	$("#data-toogle-1").click(function () {
	  $("#expand").text("Expand for game details");
       $("#data-toogle-2").show();
	   $(".table_show").hide();
	  
	   $(this).hide();

    });
	
	$('#data-toogle-2').click(function(){
   // /$("#table_show").show();
   $("#expand").text("Collapse for game details");
   $("#data-toogle-1").show();
   $(".table_show").show();
   $(this).hide();
    //
	// $('span').hide()
	// $('#data-toogle-2').show();
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
  
  $(document).ready(function(){
	  var sportName = sessionStorage.getItem('sport_name');
	  var teamName = sessionStorage.getItem('team_name');
	  if(!(sportName!=undefined && teamName!=undefined && teamName.length>0 && sportName.length>0 && teamName.length>0)){
		  sportName='nba';
		  teamName='Chicago Bulls';
	  }
	  $("#teamNameId").text(teamName);
	  sessionStorage.setItem('sport','ncaab');
	  initializeUI(sportName, teamName);
	  sessionStorage.removeItem('sport_name');
	  sessionStorage.removeItem('team_name');
  });

  });
 
 
 function initializeUI(sport,teamName){
	 var url = "/playersinfo/info/v1/teamplayers?team="+teamName+"&sport="+sport+"&game=game&line=spread&sportsbook=BOOKMAKER LINE MOVEMENTS";
		//'sportteam/'+sport+"/"+teamName
	 
	 	var headerMap={};
	 	var playerTeam;
	 	var topTblData;
		$.fn.requestCall('GET','',url,80000,function(data){
			console.log("DATA: "+data);
			var resObj = JSON.parse(data);
			playerTeam = resObj.playersData;
			topTblData = resObj.topTableData;
			headerMap = resObj.headerRow;
			setTopTableRecord(topTblData);
			setPlayersRecord(playerTeam);
		});
		
		
		function setPlayersRecord(obj){
			var trs = "";
			var ths ="";
			
			var rowno=1;
			
			
			 $("#myTable tbody").html('');
			 
			 ths +=" <tr class='a dark-blue c-white'>";
			 ths +="<th colspan='2'>Player</th>";
			 ths+="<th >Pos</th>";
             
			 ths+=" <th>Total</br>Min</th>";
			 ths+="<th>Avg</br>Min</th>";
			 ths+="  <th></br>ORating</th>";
			 ths+=" <th></br>DRating</th>";
			 
			 
			 var ddmmyyyyFormatArray = [];
			 var dateArray = [];
			 var thArrray = [];
			 for ( var k in obj) {

							if (obj[k] instanceof Object) {
								if (obj[k].games != null) {
									getDateArray(obj[k].games, dateArray);
								}
							}

						}

			 var uniqueArray = createDateArray(); //Than Singh
			 var thead ="<thead>";
			 thead +="<tr class='a bg-dark-blue c-balck'> <th colspan='6' class='c-white'>MINUTES PLAYED</th><th></th>";
			 
				$.each(uniqueArray, function(i, obj1){
					 thead +="<th>";
					thead+=changeDateFormat(obj1.gameDate);
					thead+="</th>";
					
						ths +="<th>";
				     	ths +=obj1.opponent +"<br>"+ obj1.result;
				      ths +="</th>"
				 }); 
			 thead+="</tr> </thead>";
			 ths +="</tr>";
			 
			 for(var k in obj) {
				 var id = k.replace(/\s/g, "");

				
				 if(obj[k] instanceof Object) {
					 var hoverstr = "RPG "+obj[k].rpg+", PPG "+obj[k].ppg+", APG "+obj[k].apg+", PER "+obj[k].per;
					 
						// console.log(obj[k]);
							trs += "<tr>";
							trs += "<td class='a'>"+rowno+"</td>";
							trs += "<td class='a' data-toggle='tooltip' title='"+hoverstr+"'>"+obj[k].playerName+"</td>";
							trs += "<td class='a'>"+obj[k].position+"</td>";
							 var minute=0;
						     //var second=0;
						     var oRating =obj[k].oRating;
						     var dRating = obj[k].dRating;
							if(obj[k].games!=null){
								var counter=0;
							 $.each(obj[k].games, function(i, obj){
								 //var splitTime2= obj.minutesPlayed.split(':');
							      minute = minute + parseInt(obj.minutesPlayed);
							      //second = parseInt(splitTime2[1])
							      //second=second/60;
							      if(minute!=null && minute!='NaN' && minute>0){
							        minute = minute;//+second;
							      }
							 }); 
							
							//console.log(""+dRating)
							trs += "<td class='a'>"+Math.round(minute)+"</td>";
							var mins = Math.round(Math.round(minute)/obj[k].games.length);
							trs += "<td class='a'>"+mins+"</td>"; 
							trs += "<td class='a'>"+Math.round(oRating)+"</td>"
							trs += "<td class='a'>"+Math.round(dRating)+"</td>"
							
							var count = 1;
							var count1=1;
							var checkUpto = obj[k].games.length;
							var checkForTd = true;
							$.each(uniqueArray, function(i, obj1){
								checkForTd = true;
									if(obj[k].games!=null){
										/*
										 * $.each(obj[k].games, function(j,
										 * innerObject){
										 */
											for (var j = 0; j < obj[k].games.length; j++) {
											    var innerObject = obj[k].games[j]
											if(obj1.gameDate === innerObject.gameDate){
											var minute = 0;
											//var second = 0;
											 //var splitTime2= innerObject.minutesPlayed.split(':');
										      minute = minute + parseInt(innerObject.minutesPlayed);
										      //second = parseInt(splitTime2[1])
										     // second=second/60;
										      
										      if(minute!=null && !isNaN(minute) && minute>0){
											        minute = minute;
											        //console.log("---minutes"+minute+"-----"+innerObject.minutesPlayed);
											    	trs += "<td class='a'>"+Math.round(minute)+"</td>";
											    	 checkForTd = false;
											    	 break
											      }
											      else{
											    	  
											    	//  if(innerObject.minutesPlayed!=null && !isNaN(innerObject.minutesPlayed) ){
												    	if(Number(innerObject.minutesPlayed)==0){
												    		if(mins>=20 && mins<25){
																//console.log("greater 20: "+trs);
																trs += "<td class='a' style='background:yellow; border:none'></td>";
															} else if(mins>=25 && mins<30){
																trs += "<td class='a' style='background:#dda702; border:none'></td>";
															} else if(mins>=30){
																trs += "<td class='a' style='background:red; border:none'></td>";
															} else
																trs += "<td class='a'></td>";
												    	} else{
												    		trs += "<td class='a'>"+Math.round(innerObject.minutesPlayed)+"</td>";
												    	}
												    	 checkForTd = false;
												    	 break;
											    	/*  }	else{
											    		  //console.log(innerObject.minutesPlayed)
											    		  trs += "<td class='a'>1</td>";
													    	 checkForTd = false;
													    	break;
												    	}*/
											      
											      }
											}
										 } 
										if(checkForTd){
											if(mins>=20 && mins<25){
												//console.log("greater 20: "+trs);
												trs += "<td class='a' style='background:yellow; border:none'></td>";
											} else if(mins>=25 && mins<30){
												trs += "<td class='a' style='background:#dda702; border:none'></td>";
											} else if(mins>=30){
												trs += "<td class='a' style='background:red; border:none'></td>";
											} else
												trs += "<td class='a'></td>";
										}
									}
							 }); 
					       trs +="</tr>";
		        } 
				 if(rowno==dateArray.length){
					 break;
				 }
				 rowno++;
			    }}
			 
			 
			 $("#myTable tbody").html(thead+ths+trs);	
		
		}
		
		function createDateArray(playerTeam){
			var dateArr=[];
			for(var dateKey in headerMap) {
				var jsnObj={};
				jsnObj['gameDate']=dateKey;
				jsnObj['result']=headerMap[dateKey];
				jsnObj['opponent']=getOpponent(dateKey, headerMap[dateKey]);
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
		
		function setTopTableRecord(topTblData){
			var tableStr='<table class="table table-borderless mb-0">';
			var tableRowStr="";
			var avrgJson={};
			var keySet = [ "date","opponent",
			      "result", "overtime","points",
			      "oppPoints","totalPoints", "offRating", "defRating", "possessions","closingSpread","atsResult","sideCoverMargin","closingTotal","overUnder",
			      "totalCoverMargin"];
			
			var showKey=[ "Date","Opponent",
			      "Result","Overtimes","Points",
			      "Opp Points","Total Points", "Off Rating", "Def Rating", "Possessions", "Closing Spread","ATS Results","Side Cover Margin",
			      "Closing Total","O/U Result",
			      "Total Cover Margin"];
			
			var avrgKeySet=[ "Overtimes","Points",
			      "Opp Points","Total Points", "Off Rating", "Def Rating", "Possessions", "Closing Spread","ATS Results","Side Cover Margin",
			      "Closing Total","O/U Result",
			      "Total Cover Margin"]; 
			
			for(var i=0; i<16; i++) {
				rowStr = createTableRow(topTblData,keySet[i],showKey[i],avrgJson,isExistsAvrgKey(avrgKeySet,showKey[i]));
				tableRowStr+=rowStr;
			}
			$("#part_2").html(tableStr+tableRowStr+"</table>");
			showAvrageRecord(avrgKeySet, avrgJson);
		}
		
		function isExistsAvrgKey(avrgKeySet,key){
			var status=false;
			for(var i=0;i<avrgKeySet.length;i++) {
				if(avrgKeySet[i]==key) {
					status=true; break;
				}
			}
			return status;
		}
		
		var averageovertime = 0;
		
		function showAvrageRecord(showKey, avrgJson) {
			var tableHeads="<tr><th>All Games</th><th>Average</th></tr>";
			var rowsCols="";
			for(var i=0;i<showKey.length;i++) {
				
				if(showKey[i]=='ATS Results' || showKey[i]=='O/U Result') {
					rowsCols+="<tr class='a'><td>"+showKey[i]+"</td><td>"+(avrgJson[showKey[i]])+"</td></tr>";
				} else if(showKey[i]==="Overtimes"){
					rowsCols+="<tr class='a'><td>"+showKey[i]+"</td><td>"+averageovertime+"</td></tr>";
				} else {
					rowsCols+="<tr class='a'><td>"+showKey[i]+"</td><td>"+(avrgJson[showKey[i]]).toFixed(1)+"</td></tr>";
				}
			}
			$("#gameAvrgId thead").html(tableHeads);
			$("#gameAvrgId tbody").html(rowsCols);
		}
		
		function createTableRow(topTableData, key,showKey,avrgJson,isExistsAvrgKey){
			var rowCols="<td>"+showKey+"</td>";
			var avrg=0;
			var countL=0;  //used for atsResult and overUnder Keys 
			var countPush=0; //used for atsResult
			var overtimemin=0;
			var overtimesec=0;
			var overtimestr="0:0";
			var overtimecounter=0;
			
			for(var headerKeyName in headerMap) {
				var headKeyValue = headerMap[headerKeyName];
				var index=isExistsHeaders(headerKeyName,topTableData);
				if(index!=-1) {
					var record = topTableData[index][key];
					if(record!=null && isExistsAvrgKey && !isNaN(record)) {
						avrg+=parseInt(record);
					}
					record = record!=null ? record : "-";
					record = record!="0:0" ? record : "-";
					
					if(key === "overtime"){
						if(record!="-"){
							overtimecounter++;
							var str = record.split(":");
							//console.log("Min "+str[0]);
							//console.log("Sec "+str[1]);
							overtimemin += Number(str[0]);
							var tempmin = str[1];
							if(str[1]>60){
								while(tempmin>60){
									tempmin = tempmin-60;
									overtimemin++;
								}
							}
							overtimesec = tempmin;
						}
					}
					
					if(key=='date'&&record.length==10) {
						record=record.substring(5);
					}
					
					if(key=='atsResult'){
						if(record=='L'||record=='l'){
							countL++;
						}else if(record=='push'||record=='Push'||record=='PUSH'){
							countPush++;
						}
					}
					
					if(key=='overUnder'){
						if(record=='U'||record=='u'){
							countL++;
						}
					} 
					rowCols+="<td align='right'>"+record+"</td>";
				}
			}
			
			if(key==='overtime'){
				averageovertime = (overtimemin/overtimecounter).toFixed(1);
				console.log("overtimemin: "+(overtimemin/overtimecounter).toFixed(1)+" overtimecounter "+overtimecounter);
			}
			
			rowCols+="<td>"+showKey+"</td>";
			if(isExistsAvrgKey)
				avrgJson[showKey]=avrg/topTableData.length;
			if(isExistsAvrgKey && key=='atsResult') {
				var winRslt = (topTableData.length-countPush)-countL;
				avrgJson[showKey]=winRslt+"-"+countL;
			}
			if(isExistsAvrgKey && key=='overUnder') {
				avrgJson[showKey]=(topTableData.length-countL)+"-"+countL;
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
					index=i;
					break;
				}
			}
			return index;
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
		
		function formatDate (input) {
			  var datePart = input.match(/\d+/g),
			  year = datePart[0], // get only two digits
			  month = datePart[1], day = datePart[2];

			  return year+'/'+month+'/'+day;
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
		
		function removeDuplicates(originalArray, prop) {
		     var newArray = [];
		     var lookupObject  = {};

		     for(var i in originalArray) {
		        lookupObject[originalArray[i][prop]] = originalArray[i];
		     }

		     for(i in lookupObject) {
		         newArray.push(lookupObject[i]);
		     }
		      return newArray;
		 }

		
		
		function dateToNum(d) {
			  // Convert date "26/06/2016" to 20160626
			  d = d.split("/"); return Number(d[2]+d[1]+d[0]);
			}
 }