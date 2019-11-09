/**
 * 
 */

$(document).ready(function(){
	//test code
/*	$("#testrequest").click(function(){
		var json = {
				"sports":[
					{
					"name":"nba",
					"date": "11-25-2019"
					},
					{
					"name":"mlb",
					"date": "12-20-2019"
					}],
			"userid":"suresh_023@pinesucceed.com"
		};
		
		$.fn.requestCall('POST',JSON.stringify(json),'http://3.14.246.242:9096/linehistory/auth?token=',10000,function(data){
			console.log("category : "+data);
		});
	});*/
	
	$("#by_conferance").hide();
	
	var data = sessionStorage.getItem('sport');

	if(data!=undefined && data.length>0) {
		$("#sportsname").val(data);
	}else {
		data='nba';
		$("#sportsname").val(data);
	}
	
	$("#byconf").attr('style', 'display: none !important');
	
	initializeUI(data);
	sessionStorage.removeItem('sport');
	sessionStorage.clear();
	
	$("#sportsname").change(function(){
		initializeUI($(this).val());
		var value = $(this).val();
		
		if((value=="ncaab") || (value=="ncaaf")){
			$("#byconf").css("display","block");
			changetab('conf');
		} else{
			$("#byconf").attr('style', 'display: none !important');
			changetab('alpha');
		}
	});
	///categories/nba
	
	/*$.fn.requestCall('GET','','/playersinfo/info/v1/categories/nba',10000,function(data){
		console.log("category : "+data);
	});
	*/
});


function initializeUI(sport){
	
	$.fn.requestCall('GET','','/playersinfo/info/v1/categorywise/'+sport,10000,function(data){
		console.log(data);
		
		var obj = JSON.parse(data);	
		var trs = "";
		var tabletrs = "";
		var rowno=0;
		var options = "";
		var confoptions = "";
		
		$("#teamtable tbody").html('');
		$("#conftable tbody").html('');
		
		var array = [];
		 for(var k in obj) {
			 
			 var id = k.replace(/\s/g, "");
			 trs += "<tr><td class='font-first' >"+k+"</td><td id='"+id+"'></td></tr>";
			 confoptions += "<option value="+k+">"+k+"</option>";
			 
			 var average = 0;
			 var count = 0;
			 var sum=10;
				 if(obj[k] instanceof Object) {
					 $.each(obj[k], function(index, val){
						count++;
						sum+=10
						rowno++;
						
						if(val.frontName != null){
							tabletrs += "<tr><td>"+val.ancherTeam+"</td><td>"+k+"</td><td>1."+rowno+"</td></tr>";
							options += "<option value="+val.name+">"+val.frontName+"</option>";
							
							trs += "<tr><td class=''>"+val.ancherTeam+"</td><td>"+index+"</td></tr>";
						}
							
					 });
		        } else {
		        	console.log("else> "+obj[k] + "<br>");
		        }; 
		        
		        average = sum/count;
		        array.push(id+","+average);
		    }
		 	

		 $("#teamtable tbody").html(tabletrs);
		 $("#teams").html(options);
		 $("#conferences").html(confoptions);
		 
		 $("#conftable tbody").html(trs);
		 
		 $.each(array, function(i, obj){
			 var str = obj.split(",");
			 $("#"+str[0]).text(Number(str[1]).toFixed(2));
		 });   
	  });
}

function changetab(type){
	
	$( ".menus li" ).each(function() {
		$( this ).removeClass( "active" );
	});
	
	if(type === "alpha"){
		$("#byteams").addClass("active");
		$("#by_conferance").hide();
		$("#teamtable").show();
	} else if(type === "conf"){
		$("#byconf").addClass("active");
		$("#by_conferance").show();
		$("#teamtable").hide();
	}
}

function loaddata(sportName, teamName){
	sessionStorage.setItem('sport_name', sportName);
	sessionStorage.setItem('team_name',teamName);
	window.location.href=sportName;
}