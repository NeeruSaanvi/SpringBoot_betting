<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="ISO-8859-1"%>
<!doctype html>
<html lang="en" class="fontawesome-i2svg-active fontawesome-i2svg-complete">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>NBA</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/all.css">
<script src="js/jquery.min.js"></script>
<script src="js/popper.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/common.js"></script>
<script src="js/all.js"></script>
<script src="js/nba.js"></script>
<style>
	.table-first{width:100%;}
	.table_show {
    display: inline-block;
    width: 100%;
}
.scroll-table-container {
border:2px solid green; 
overflow: scroll;
}
.scroll-table, td, th {
border-collapse:collapse; 
border:1px solid #777; 
}
.my-custom-scrollbar {
position: relative;
height: 300px;
overflow: auto;
}
.table-wrapper-scroll-y {
display: block;
}

.a {
    white-space: nowrap;
}


table.a.table.table-borderless.mb-0 td {
    padding: 0px;
    border-right: 1px solid black;
}

table.a.table.table-borderless.mb-0 td {
    padding-bottom: 8px;
}
</style>
</head>
<body>
<div class="wrapper_container"> 
  <!-- header -->
  <header class="pt-3 pb-3">
    <div class="container">
      <div class="row"> 
        <!-- <div class="col-12 col-sm-6 col-md-3 col-lg-2 width-50"> -->
        
        <div class="col-12 col-sm-6 col-md-3 col-lg-3 width-50">
          <div class="float-lg-left float-md-right pl-3">
            <div class="tabler_heading ">
              <h3 class="c-white r-m ">Injury Grid <span class="team_name c-red d-block" id="teamNameId"></span> </h3>
            </div>
          </div>
        </div>
        
        <div class="float-sm-left pl-3 col-sm-6 col-md-2">
          <div class="logo"> <a href="#"><img src="images/logo.png" class="img-fluid" alt="NBA"></a> </div>
        </div>
        
        <!-- logo -->
        
        
        <!-- color picker -->
        <div class="col-12 col-sm-12 col-md-7 col-lg-7 clm-reverse float-right">
          <!--<div class="dropdown d-inline-block">
            <button type="button" class="btn toggle-btn blue c-white dropdown-toggle" data-toggle="dropdown" aria-expanded="false">- Collapse game details</button>
            <div class="dropdown-menu table-first" x-placement="bottom-start" style="position: absolute; will-change: transform; top: 0px; left: 0px; transform: translate3d(0px, 112px, 0px);">
              <div class="table_show">
                <table class="table table-borderless mb-0 light">
                  <thead class="bg-white text-center">
                    <tr>
                      <th>All Games</th>
                      <th>Average</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td>Overtimes</td>
                      <td>1</td>
                    </tr>
                    <tr>
                      <td>Points</td>
                      <td>99.7</td>
                    </tr>
                    <tr>
                      <td>Opp Points</td>
                      <td>...</td>
                    </tr>
                    <tr>
                      <td>Total Points</td>
                      <td>1</td>
                    </tr>
                    <tr>
                      <td>Off Rating</td>
                      <td>99.7</td>
                    </tr>
                    <tr>
                      <td>Def Rating</td>
                      <td>...</td>
                    </tr>
                    <tr>
                      <td>Possessions</td>
                      <td>1</td>
                    </tr>
                    <tr>
                      <td>Closing Spread</td>
                      <td>+6.2</td>
                    </tr>
                    <tr>
                      <td>ATS Results</td>
                      <td>0-3</td>
                    </tr>
                    <tr>
                      <td>Side Cover Margin</td>
                      <td>-2.5</td>
                    </tr>
                    <tr>
                      <td>Closing Total</td>
                      <td>...</td>
                    </tr>
                    <tr>
                      <td>O/U Results</td>
                      <td>1-2</td>
                    </tr>
                    <tr>
                      <td>Total Cover Margin</td>
                      <td>-3.8</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>-->
          <div class="color_select">
            <ul>
              <li><a href="#" class="c-white font-16 r-m"><span class="bg-red mr-2"></span>Player missed game and averages 30+ MPG</a></li>
              <li><a href="#" class="c-white font-16 r-m"><span class="bg-yellow mr-2"></span>Player missed game and averages 25-30 MPG</a></li>
              <li><a href="#" class="c-white font-16 r-m"><span class="bg-y-light mr-2"></span>Player missed game and averages 20-25 MPG</a></li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </header>
  
  <!-- end header --> 
  
  <!-- table -->
  <div class="table_wrapper">
    <div class="container">
      <div class="w-100 d-inline-block">
        <div class="table-first">
          <ul class="mb-3">
            <li> <a href="#" id="data-toogle-1" class="c-white font-14 r-m"><span>- Collapse game details</span></a> </li>
             <li> <a href="#" id="data-toogle-2" class="c-white font-14 r-m"><span>+ Expand for game details</span></a> </li>
           		<!-- <span id="expand">-+ Expand for game details</span> -->
          </ul>
          <div class="table_show" style="margin-left: 75px">
           <div class="table_col">
            <table class="table table-borderless mb-0 light" id="gameAvrgId" style="margin-top: 49px">
              <thead class="bg-white text-center">
             
              </thead>
              <tbody>
                
              </tbody>
            </table>
            </div>
            <div class="table_col_2">
            	<div class="table-container">
         <!--  <div class="left-arrow">
            <button type="button" id="btn_1" class="btn-1" value="2"><i class="fas fa-angle-double-left"></i></button>
          </div>
          <div class="right-arrow">
            <button type="button" id="btn_2" class="btn-2" value="1"><i class="fas fa-angle-double-right"></i></button>
          </div> -->
          <div id="part_2">
           
          </div>
          <div id="part_1">
            
          </div>
        </div>
        	</div>
        </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  
  <!-- table 2 -->
  <div class="tale_2 container">
    <div class="row">
      <div class="col-12">
        <!-- <div class="left-arrow">
          <button type="button"  id="btn_3" class="btn-1" value="2"><i class="fas fa-angle-double-left"></i></button>
        </div>
        <div class="right-arrow">
          <button type="button" id="btn_4" class="btn-2" value="1"><i class="fas fa-angle-double-right"></i></button>
        </div> -->
        <div class="table_width" id="tab_1">
          <table class="table">
            <thead>
              <tr class="bg-dark-blue c-balck">
                <th colspan="6" class="c-white">MINUTES PLAYED</th>
                <th>10/19</th>
                <th>10/21</th>
                <th>10/24</th>
                <th>10/26</th>
                <th>10/28</th>
                <th>11/01</th>
                <th>11/03</th>
                <th>11/04</th>
                <th>11/07</th>
                <th>11/10</th>
              </tr>
            </thead>
            <tbody>
              <tr class="dark-blue c-white">
                <th colspan="2">Player</th>
                <th>Pos</th>
                <th>Total Min</th>
                <th>Avg Min</th>
                <th>O Rating</th>
                <th>D Rating</th>
                <th>@TOR L97-113</th>
                <th>SAS L98-109</th>
                <th>@CLE W104-103</th>
                <th>ATL</th>
                <th>OKC</th>
                <th>@MIA</th>
                <th>@ORL</th>
                <th>NOR</th>
                <th>@TOR</th>
              </tr>
              <tr>
                <td>01</td>
                <td class="a">Antonio Blakeneyz</td>
                <td class="a">c</td>
                <td class="a">786</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
                <td class="a">38</td>
                <td class="a">29</td>
                <td class="a">43</td>
                <td style="background: red"></td>
              </tr>
              <tr>
                <td>01</td>
                <td class="a">Quincy Pondexter</td>
                <td class="a">c</td>
                <td class="a">786</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
                <td class="a">38</td>
                <td class="a">29</td>
                <td class="a">43</td>
                <td style="background: red"></td>
              </tr>
              <tr>
                <td>01</td>
                <td class="a">Kay Felder</td>
                <td class="a">c</td>
                <td class="a">786</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
                <td class="a">38</td>
                <td class="a">29</td>
                <td class="a">43</td>
                <td style="background: red"></td>
              </tr>
              <tr>
                <td>01</td>
                <td class="a">Nikola Mirotic</td>
                <td class="a">c</td>
                <td class="a">786</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
                <td class="a">38</td>
                <td class="a">29</td>
                <td class="a">43</td>
                <td style="background: red"></td>
              </tr>
              <tr>
                <td>01</td>
                <td class="a">Ryan Arcidiacono</td>
                <td class="a">c</td>
                <td class="a">786</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
                <td class="a">38</td>
                <td class="a">29</td>
                <td class="a">43</td>
                <td style="background: red"></td>
              </tr>
              <tr>
                <td>01</td>
                <td class="a">Robin Lopez</td>
                <td class="a">c</td>
                <td class="a">786</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
                <td class="a">38</td>
                <td class="a">29</td>
                <td class="a">43</td>
                <td style="background: red"></td>
              </tr>
            <tfoot>
              <tr class="bg-dark-blue c-balck">
                <th></th>
                <th colspan="3">Minutes Lost</th>
                <th>56</th>
                <th></th>
                <th></th>
                <th>56</th>
                <th>56</th>
                <th>56</th>
                <th>41</th>
                <th></th>
                <th>71</th>
                <th>71</th>
                <th></th>
                <th></th>
              </tr>
            </tfoot>
              </tbody>
            
          </table>
        </div>
        <div class="table_width" id="tab_2">
          <table class="table" id="myTable">
          <!--   <thead>
              <tr class="bg-dark-blue c-balck">
                <th>11/11</th>
                <th>11/15</th>
                <th>11/17</th>
                <th>11/19</th>
                <th>11/15</th>
                <th>11/17</th>
                <th>11/19</th>
                <th>11/21</th>
                <th>11/22</th>
                <th>11/24</th>
                <th>11/26</th>
                <th>11/28</th>
                <th>11/30</th>
                 <th>12/01</th>
                <th>12/04</th>
                <th>12/06</th>
              </tr>
            </thead> -->
            <tbody>
             <!--  <tr class="dark-blue c-white">
                <th colspan="2">Player</th>
                <th>Pos</th>
                <th>Total Min</th>
                <th>Avg Min</th>
                <th>O Rating</th>
                <th>D Rating</th>
                <th>@TOR L97-113</th>
                <th>SAS L98-109</th>
                <th>@CLE W104-103</th>
                <th>ATL</th>
                <th>OKC</th>
                <th>@MIA</th>
                <th>@ORL</th>
                <th>NOR</th>
                <th>@TOR</th>
              </tr>
              <tr>
                <td>01</td>
                <td class="a">Justin Holiday</td>
                <td class="a">c</td>
                <td class="a">786</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
                <td class="a">38</td>
                <td class="a">29</td>
                <td class="a">43</td>
                <td style="background: red"></td>
              </tr>
              <tr>
                <td>01</td>
                <td class="a">Lauri Markkanen</td>
                <td class="a">c</td>
                <td class="a">786</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
                <td class="a">38</td>
                <td class="a">29</td>
                <td class="a">43</td>
                <td style="background: red"></td>
              </tr>
              <tr>
                <td>01</td>
                <td class="a">Denzel Valentine</td>
                <td class="a">c</td>
                <td class="a">786</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
                <td class="a">38</td>
                <td class="a">29</td>
                <td class="a">43</td>
                <td style="background: red"></td>
              </tr>
              <tr>
                <td>01</td>
                <td class="a">Jerian Grant</td>
                <td class="a">c</td>
                <td class="a">786</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
                <td class="a">38</td>
                <td class="a">29</td>
                <td class="a">43</td>
                <td style="background: red"></td>
              </tr>
              <tr>
                <td>01</td>
                <td class="a">David Nwaba</td>
                <td class="a">c</td>
                <td class="a">786</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
                <td class="a">38</td>
                <td class="a">29</td>
                <td class="a">43</td>
                <td style="background: red"></td>
              </tr>
              <tr>
                <td>01</td>
                <td class="a">Cristiano Felicio</td>
                <td class="a">c</td>
                <td class="a">786</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
                <td class="a">38</td>
                <td class="a">29</td>
                <td class="a">43</td>
                <td style="background: red"></td>
              </tr>
            <tfoot>
              <tr class="bg-dark-blue c-balck">
                <th></th>
                <th colspan="3">Minutes Lost</th>
                <th>56</th>
                <th></th>
                <th></th>
                <th>56</th>
                <th>56</th>
                <th>56</th>
                <th>41</th>
                <th></th>
                <th>71</th>
                <th>71</th>
                <th></th>
                <th></th>
              </tr>
            </tfoot> -->
              </tbody>            
          </table>
        </div>
      </div>
    </div>
  </div>


<script type="text/javascript">

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


 // $("#tab_1").hide();
//    $("#btn_3").click(function(){
//      $("#tab_2").hide();
//      $("#tab_1").show("slide",{direction: 'right'}, 1000);
//    });
//    $("#btn_4").click(function(){
//      $("#tab_1").hide();
//      $("#tab_2").show("slide",{direction: 'left'}, 1000);
//    });
  });
</script>
</body>
</html>
