
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


  // $("#tab_1").hide();
// $("#btn_3").click(function(){
// $("#tab_2").hide();
// $("#tab_1").show("slide",{direction: 'right'}, 1000);
// });
// $("#btn_4").click(function(){
// $("#tab_1").hide();
// $("#tab_2").show("slide",{direction: 'left'}, 1000);
// });
  });
 $(document).ready(function(){
	  var sportName = sessionStorage.getItem('sport_name');
	  var teamName = sessionStorage.getItem('team_name');
	  if(!(sportName!=undefined && teamName!=undefined && teamName.length>0 && sportName.length>0 && teamName.length>0)){
		  sportName='nhl';
		  teamName='Vegas Golden Knights';
	  }
	  $("#teamNameId").text(teamName);
	  initializeUI(sportName, teamName);
	  sessionStorage.removeItem('sport_name');
	  sessionStorage.removeItem('team_name');
});

 
 
 function initializeUI(sport,teamName){
		
	 var url = "/playersinfo/info/v1/teamplayers?team="+teamName+"&sport="+sport+"&game=game&line=spread&sportsbook=BOOKMAKER LINE MOVEMENTS";
		$.fn.requestCall('GET','',url,30000,function(data){
			console.log(data);
			
			var obj = JSON.parse(data);
			
			var resObj = JSON.parse(data);
			playerTeam = resObj.playersData;
			topTblData = resObj.topTableData;
			headerMap = resObj.headerRow;
			setTopTableRecord(topTblData);
			setPlayersRecord(playerTeam);
		});
		
		function setTopTableRecord(topTblData) {
			var tableStr='<table class="a table table-borderless mb-0">';
			var tableRowStr="";
			var avrgJson={};
			var keySet = [ "date", "opponent","goals","oppGoals","shotsonGoal","oppShotsonGoal","closingML","puckLineSpread","closingSpread",
							"runLineResult","closingTotal","overUnder","totalCoverMargin"];
			
			var showKey=[ "Date", "Opponent", "Goals", "Opp Goals","Shots on Gaol", 
						  "Opp Shots on Goal", "Closing ML", "Puck Line Spread","Closing Puck Line",
						  "Puck Line Result","Closing Total","O/U Result","Total Cover Margin"];
			
			var avrgKeySet=["Goals", "Opp Goals","Shots on Gaol", 
				  			"Opp Shots on Goal", "Closing ML", "Puck Line Spread","Closing Puck Line",
				  			"Puck Line Result","Closing Total","O/U Result","Total Cover Margin"]; 
			
			for(var i=0; i<13; i++) {
				rowStr = createTableRow(topTblData,keySet[i],showKey[i],avrgJson,isExistsAvrgKey(avrgKeySet,showKey[i]));
				tableRowStr+=rowStr;
			}
			$("#part_2").html(tableStr+tableRowStr+"</table>");
			showAvrageRecord(avrgKeySet, avrgJson);
		}
		
		function createTableRow(topTableData, key,showKey,avrgJson,isExistsAvrgKey) {
			var rowCols="<td>"+showKey+"</td>";
			var avrg=0;
			
			for(var headerKeyName in headerMap) {
				var headKeyValue = headerMap[headerKeyName];
				var index=isExistsHeaders(headerKeyName,topTableData);
				if(index!=-1) {
					var record = topTableData[index][key];
					if(record!=null && isExistsAvrgKey && !isNaN(record)) {
						avrg+=parseInt(record);
					}
					record = record!=null ? record : "-";
					
					if(key=='date'&&record.length==10) {
						record=record.substring(5);
					}
					
					rowCols+="<td>"+record+"</td>";
				}
			}
			if(isExistsAvrgKey)
				avrgJson[showKey]=avrg/topTableData.length;
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
			var tableHeads="<tr><th>All Games</th><th>Average</th></tr>";
			var rowsCols="";
			for(var i=0;i<showKey.length;i++) {
				
				if(showKey[i]=='ATS Results' || showKey[i]=='O/U Result') {
					rowsCols+="<tr class='a'><td>"+showKey[i]+"</td><td>"+(avrgJson[showKey[i]])+"</td></tr>";
				}else {
					rowsCols+="<tr class='a'><td>"+showKey[i]+"</td><td>"+(avrgJson[showKey[i]]).toFixed(1)+"</td></tr>";
				}
			}
			$("#gameAvrgId thead").html(tableHeads);
			$("#gameAvrgId tbody").html(rowsCols);
		}
		
		function setPlayersRecord(obj){
			var trs = "";
			var ths ="";
			var rowno=1;
			 $("#myTable tbody").html('');
			 
			 ths +=" <tr class='a dark-blue c-white'>";
			 ths +="<th colspan='2'>Player</th>";
			 ths+="<th >Pos</th>";
             
			 ths+=" <th>Total Min</th>";
			 ths+="<th>Avg. Min</th>";
			
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
			 
			 //ddmmyyyyFormatArray = [];
			 
			
			//var uniqueArray = removeDuplicates(dateArray, "gameDate");
			var uniqueArray = createDateArray();
			/*$.each(uniqueArray, function(i, obj){
				 ddmmyyyyFormatArray.push(changeDateFormat(obj.gameDate));
				 });*/  
			/*
			 * $.each(dateArray, function(j, obj2){ ths +="<th>"; ths
			 * +=obj2.opponenet+""+obj2.gameDate; ths +="</th>" });
			 */
			
			 
			 
			 var thead ="<thead>";
			 thead +="<tr class='a bg-dark-blue c-balck'> <th colspan='5' class='c-white'>MINUTES PLAYED</th>";
			 uniqueArray.sort(function(a,b){
				  return +new Date(a.gameDate) - +new Date(b.gameDate);
				});
             
				$.each(uniqueArray, function(i, obj1){
					 thead +="<th>";
					thead+=changeDateFormat(obj1.gameDate);
					thead+="</th>"
						ths +="<th>";
				      ths +=obj1.opponent +"<br>"+ obj1.result;
				      ths +="</th>"
				 }); 
			 thead+="</tr> </thead>";
			 ths +="</tr>";
			 
			 for(var k in obj) {
				 var id = k.replace(/\s/g, "");

				
				 if(obj[k] instanceof Object) {
					 
						// console.log(obj[k]);
							trs += "<tr>";
							trs += "<td class='a'>"+rowno+"</td>";
							trs += "<td class='a'>"+obj[k].playerName+"</td>";
							trs += "<td class='a'>"+obj[k].position+"</td>";
							 var minute=0;
						     var second=0;
						     var oRating =0;
						     var dRating = 0;
							if(obj[k].games!=null){
							 $.each(obj[k].games, function(i, obj){
								 var splitTime2= obj.minutesPlayed.split(':');
							      minute = minute + parseInt(splitTime2[0]);
							      second = parseInt(splitTime2[1])
							      second=second/60;
							      if(second!=null && second!='NaN' && second>0){
							        minute = minute+second;
							      }
							 }); 
							
							
							trs += "<td class='a'>"+Math.round(minute)+"</td>"; 
							var mins = Math.round(Math.round(minute)/obj[k].games.length);
							trs += "<td class='a'>"+Math.round(Math.round(minute)/obj[k].games.length)+"</td>"; 
							
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
											var second = 0;
											 var splitTime2= innerObject.minutesPlayed.split(':');
										      minute = minute + parseInt(splitTime2[0]);
										      second = parseInt(splitTime2[1])
										      second=second/60;
										      
										      if(minute!=null && !isNaN(minute) && minute>0){
											        minute = minute+second;
											        //console.log("---minutes"+minute+"-----"+innerObject.minutesPlayed);
											    	trs += "<td class='a'>"+Math.round(minute)+"</td>";
											    	 checkForTd = false;
											    	 break
											      }
											      else{
											    	  if(innerObject.minutesPlayed!=null && !isNaN(innerObject.minutesPlayed) ){
											    		  console.log("nhl "+Number(innerObject.minutesPlayed));	
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
											    	  }	else {
												    		trs += "<td class='a'>1</td>";
													    	 checkForTd = false;
													    	break;
												    	}
											      }
											}
										 } 
										if(checkForTd){
											 // if(Number(innerObject.minutesPlayed)==0){
										    		if(mins>=20 && mins<25){
														//console.log("greater 20: "+trs);
														trs += "<td class='a' style='background:yellow; border:none'></td>";
													} else if(mins>=25 && mins<30){
														trs += "<td class='a' style='background:#dda702; border:none'></td>";
													} else if(mins>=30){
														trs += "<td class='a' style='background:red; border:none'></td>";
													} else
														trs += "<td class='a'></td>";
										    	//}else 
										    		//trs += "<td class='a'></td>";
										}
									}
							 }); 
					       trs +="</tr>";
						}
		        } 
				 if(rowno==dateArray.length){
					 break;
				 }
				 rowno++;
			    }
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