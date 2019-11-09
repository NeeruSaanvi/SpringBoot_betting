
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
		  sportName='mlb';
		  teamName='Chicago Bulls';
	  }
	  $("#teamNameId").text(teamName);
	  sessionStorage.setItem('sport','mlb');
	  initializeUI(sportName, teamName);
	  sessionStorage.removeItem('sport_name');
	  sessionStorage.removeItem('team_name');
 });
 
 
 function initializeUI(sport,teamName){
	 var url = "/playersinfo/info/v1/teamplayers?team="+teamName+"&sport="+sport+"&game=game&line=spread&sportsbook=BOOKMAKER LINE MOVEMENTS";
		$.fn.requestCall('GET','',url,10000,function(data){
			console.log(data);
			var obj = JSON.parse(data);
			
			var resObj = JSON.parse(data);
			playerTeam = resObj.playersData;
			topTblData = resObj.topTableData;
			headerMap = resObj.headerRow;
			//setTopTableRecord(topTblData);
			//setPlayersRecord(playerTeam);
		});
		
		function setTopTableRecord(topTblData){
			var tableStr='<table class="table table-borderless mb-0">';
			var tableRowStr="";
			var avrgJson={};
			var keySet = [ "date", "opponent","goals","oppGoals","shotsonGoal","oppShotsonGoal","closingML","puckLineSpread","closingSpread",
				"runLineResult","closingTotal","overUnder","totalCoverMargin"];

			var showKey=[ "Date", "Opponent", "Starting Pitcher", "Opp Starting Pitcher","Runs Scored", 
			  "Opp Runs", "Closing ML", "Run Line Spread","Closing Run Line",
			  "Run Line Result","Closing Total","O/U Result","Total Cover Margin"];

			var avrgKeySet=["Runs Scored",
				  "Opp Runs", "Closing ML", "Run Line Spread","Closing Run Line",
				  "Run Line Result","Closing Total","O/U Result","Total Cover Margin"]; 
			
			for(var i=0; i<showKey.length; i++) {
				rowStr = createTableRow(topTblData,keySet[i],showKey[i],avrgJson,isExistsAvrgKey(avrgKeySet,showKey[i]));
				tableRowStr+=rowStr;
			}
			$("#part_2").html(tableStr+tableRowStr+"</table>");
			showAvrageRecord(avrgKeySet, avrgJson);
		}
		
		function createTableRow(topTableData, key,showKey,avrgJson,isExistsAvrgKey){
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
					
					rowCols+="<td align='right'>"+record+"</td>";
				}
			}
			rowCols+="<td>"+showKey+"</td>";
			if(isExistsAvrgKey)
				avrgJson[showKey]=avrg/topTableData.length;
			return "<tr class='a'>"+rowCols+"</tr>";
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
		
		function setPlayerRecord(){
			var trs = "";
			var ths ="";
			
			var rowno=1;
			
			
			 $("#myTable tbody").html('');
			 
			 ths +=" <tr class='dark-blue c-white'>";
			 ths +="<th colspan='2'>Player</th>";
			 ths+="<th >Pos</th>";
             
			 ths+=" <th>Total Min</th>";
			 ths+="<th>Avg. Min</th>";
			 ths+="  <th>ORating</th>";
			 ths+=" <th>DRating</th>";
			 
			 
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
			 
			 ddmmyyyyFormatArray = [];
			 
			
			var uniqueArray = removeDuplicates(dateArray, "gameDate");
			 $.each(uniqueArray, function(i, obj){
				 ddmmyyyyFormatArray.push(changeDateFormat(obj.gameDate));
				 });  
			/*
			 * $.each(dateArray, function(j, obj2){ ths +="<th>"; ths
			 * +=obj2.opponenet+""+obj2.gameDate; ths +="</th>" });
			 */
			
			 
			 
			 var thead ="<thead>";
			 thead +="<tr class='bg-dark-blue c-balck'> <th colspan='6' class='c-white'>MINUTES PLAYED</th><th></th>";
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
					 
						 console.log(obj[k]);
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
							      if((obj.drating!=undefined && obj.drating!=null )&& obj.drating>0){
							    	  dRating = dRating + parseInt(obj.drating)
							      }
							      if((obj.orating!=undefined && obj.orating!=null )&& obj.orating>0){
							    	  oRating = oRating + parseInt(obj.orating)
							      }
							      
							 }); 
							}
							
							trs += "<td class='a'>"+Math.round(minute)+"</td>"; 
							trs += "<td class='a'>"+Math.round(Math.round(minute)/obj[k].games.length)+"</td>"; 
							trs += "<td class='a'>"+Math.round(dRating)+"</td>"
							trs += "<td class='a'>"+Math.round(oRating)+"</td>"
							
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
											        console.log("---minutes"+minute+"-----"+innerObject.minutesPlayed);
											    	trs += "<td class='a'>"+Math.round(minute)+"</td>";
											    	 checkForTd = false;
											    	 break
											      }
											      else{

											    	  if(innerObject.minutesPlayed!=null && !isNaN(innerObject.minutesPlayed) ){
												    	trs += "<td class='a'>"+Math.round(innerObject.minutesPlayed)+"</td>";
												    	 checkForTd = false;
												    	break;
											    	  }
												    	else{
												    		trs += "<td class='a'>"+0+"</td>";
													    	 checkForTd = false;
													    	break;
												    	}
											      
											      }
											}
										 } 
										if(checkForTd){
											trs += "<td class='a'>"+"-"+"</td>";
										}
									}
							 }); 
					       trs +="</tr>";
		        } 
				 if(rowno==dateArray.length){
					 break;
				 }
				 rowno++;
			    }
			 
			 
			 $("#myTable tbody").html(thead+ths+trs);
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
			console.log(inputDate)
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
		
		
		
		
		
		
		function defaultPageSet() {
			var avgTableHead =" <tr><th>All Games</th><th>Average</th></tr>";
			var avgTableBody = +"<tr><td>Runs Scored</td><td>1</td></tr>"
								+"<tr><td>Opp Runs</td><td>1</td></tr>"
								+"<tr><td>Closing ML</td><td>1</td></tr>"
								+"<tr><td>Puck Line Spread</td><td>1</td></tr>"
								+"<tr><td>Closing Puck Line</td><td>1</td></tr>"
								+"<tr><td>Puck Line Result</td><td>1</td></tr>"
								+"<tr><td>Closing Total</td><td>1</td></tr>"
								+"<tr><td>O/U Result</td><td>1</td></tr>"
								+"<tr><td>Total Cover Margin</td><td>1</td></tr>"
                			
			var topTable="<table class=table table-borderless mb-0>";
			var topTableRow=" <tr><th>Date</th>"
				                +"<td>10/19</td>"
				                +"</tr>"
				                +"<tr>"
				                +"<th>Opponent</th>"
				                +"<td>10/19</td>"
                
				                +"</tr>"
				                +"<tr>"
				                +"<th>Starting Pitcher</th>"
				                +" <td>10/19</td>"
				                
				                +" </tr>"
				                +"<tr>"
				                +"<th>Opp Starting Pitcher</th>"
				                +"<td>10/19</td>"
				                
				                +"</tr>"
				                +" <tr>"
				                +"<th>Runs Scored</th>"
				                +"<td>10/19</td>"
				                
				                +"</tr>"
				                +" <tr>"
				                +" <th>Opp Runs</th>"
				                +"  <td>10/19</td>"
				                
				                +" </tr>"
				                +"<tr>"
				                +" <th>Closing ML</th>"
				                +"<td>10/19</td>"
				                
				                +" </tr>"
				                +"<tr>"
				                +"<th>Run Line Spread</th>"
				                +" <td>10/19</td>"
				                
				                +"</tr>"
				                +"<tr>"
				                +" <th>Closing Run Line</th>"
				                +" <td>10/19</td>"
				                
				                +" </tr>"
				                +"<tr>"
				                +" <th>Run Line Result</th>"
				                +"<td>10/19</td>"
				                
				                +" </tr>"
				                +"<tr>"
				                +" <th>Closing Total</th>"
				                +" <td>10/19</td>"
				                
				                +"</tr>"
				                +"<tr>"
				                +" <th>O/U Result</th>"
				                +" <td>10/19</td>"
				                
				                +"</tr>"
				                +"<tr>"
				                +" <th>Total Cover Margin</th>"
				                +"<td>10/19</td>";
                			
			
			var playerHead= "<tr class='bg-dark-blue c-balck'>"
                			+"<th colspan='6' class='c-white'>MINUTES PLAYED</th>"
                			+"<th>10/6</th>"
                			+"<th>10/7</th>"
                			+"<th>10/9</th>"
                			+"<th>10/10</th>"
                			+"<th></th>"
                			+"<th></th>";
			
			var playerBody=" <tr class='dark-blue c-white'>"
                +"<th colspan='2'>Player</th>"
                +"<th >Pos</th>"
                +"<th>Total Min</th>"
                +"<th>Avg. Min</th>"
                +"<th>@DAL W 6-4</th>"
                +"<th>ARI</th>"
                +"<th>DET</th>"
                +"<th></th>"
                +"<th></th>"
                +"<th></th>"
                +"<th></th></tr>"
                +"<tr>"
                +"<td>7</td>"
                +"<td class='a'>Robin Lopez</td>"
                +"<td class='a'>652</td>"
                +"<td class='a'>626</td>"
                +"<td class='a'>30.0</td>"
                +"<td class='a'>104</td>"
                +"<td>107</td>"
                +"<td class='a'>35</td>"
                +"<td class='a'>36</td>"
                +"<td class='a'>37</td>"
                +"<td class='a'>32</td>"
                +"<td class='a'>32</td>"
              +"</tr>"
              
            +"<tfoot>"
              +"<tr class='bg-dark-blue c-balck'>"
              +"<th></th>"
                +"<th colspan='3'>Minutes Lost</th>"
                +"<th>56</th>"
                +"<th></th>"
                +"<th></th>"
                +"<th>56</th>"
                +"<th>56</th>"
                +"<th>56</th>"
                +"<th>41</th>"
                +"<th>71</th>"
                +"</tr>"
            +"</tfoot>";
			
			$("#gameAvrgId thead").html(avgTableHead);
			$("#gameAvrgId tbody").html(avgTableBody);
			$("#myTable tbody").html(playerHead+playerBody);
		}
 }