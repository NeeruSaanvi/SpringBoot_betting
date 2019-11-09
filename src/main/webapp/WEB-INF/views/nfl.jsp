<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>NFL</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/all.css">
<script src="js/jquery.min.js"></script>
<script src="js/popper.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/common.js"></script>
<script src="js/all.js"></script>
 <script src="js/nfl.js"></script>
<style>
	.table-first{width:100%;}
	.table_show {
    display: inline-block;
    width: 100%;
}
.table_col {
    width: 16.66%;
    float: left;
}
.table_col_2 {
    display: inline-block;
    width: 83%;
}

.a {
    white-space: nowrap;
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
        <div class="float-sm-left pl-3 col-sm-6 col-md-2">
          <div class="logo"> <a href="#"><img src="images/logo.png" class="img-fluid" alt="NFL"></a> </div>
        </div>
        
        <!-- logo -->
        <div class="col-12 col-sm-6 col-md-3 col-lg-5 width-50">
          <div class="float-lg-left float-md-right pl-3">
            <div class="tabler_heading ">
              <h3 class="c-white r-m">Injury Grid <span class="team_name c-yellow d-block" id="teamNameId"></span> </h3>
            </div>
          </div>
        </div>
        
        <!-- color picker -->
        <div class="col-12 col-sm-12 col-md-7 col-lg-5 clm-reverse">
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
            <table class="table table-borderless mb-0 light">
              <thead class="bg-white text-center" id="gameAvrgId" style="margin-top: 49px">
                 <tr>
                  <th>All Games</th>
                  <th>Average</th>
                </tr>
              </thead>
              <tbody id="averagetr">
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
                  <td>Offensive YPP</td>
                  <td>1</td>
                </tr>
                <tr>
                  <td>Defensive YPP</td>
                  <td>99.7</td>
                </tr>
                <tr>
                  <td>Closing Spread</td>
                  <td>...</td>
                </tr>
                <tr>
                  <td>ATS Results</td>
                  <td>1</td>
                </tr>
                <tr>
                  <td>Side Cover Margin</td>
                  <td>+6.2</td>
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
            <div class="table_col_2">
            	<div class="table-container">
          <div class="left-arrow">
           <!--  <button type="button" id="btn_1" class="btn-1" value="2"><i class="fas fa-angle-double-left"></i></button> -->
          </div>
          <div class="right-arrow">
           <!--  <button type="button" id="btn_2" class="btn-2" value="1"><i class="fas fa-angle-double-right"></i></button> -->
          </div>
          <div id="part_2">
           <!--  <table class="table table-borderless mb-0">
              <tr> 
                <th>Week</th>
                <td>10/19</td>
                <td>10/21</td>
                <td>10/24</td>
                <td>10/26</td>
                <td>10/28</td>
                <td>11/1</td>
                <td>11/03</td>
                <td>11/4</td>
                <td>11/7</td>
                <td>11/10</td>
                <td>11/11</td>
                <td>11/15</td>
                <td>11/17</td>
              </tr>
              <tr>
                <th>Opponent</th>
                <td>10/19</td>
                <td>10/21</td>
                <td>10/24</td>
                <td>10/26</td>
                <td>10/28</td>
                <td>11/1</td>
                <td>11/03</td>
                <td>11/4</td>
                <td>11/7</td>
                <td>11/10</td>
                <td>11/11</td>
                <td>11/15</td>
                <td>11/17</td>
              </tr>
              <tr>
                <th>Points</th>
                <td>10/19</td>
                <td>10/21</td>
                <td>10/24</td>
                <td>10/26</td>
                <td>10/28</td>
                <td>11/1</td>
                <td>11/03</td>
                <td>11/4</td>
                <td>11/7</td>
                <td>11/10</td>
                <td>11/11</td>
                <td>11/15</td>
                <td>11/17</td>
              </tr>
              <tr>
                <th>Offensive YPP</th>
                <td>10/19</td>
                <td>10/21</td>
                <td>10/24</td>
                <td>10/26</td>
                <td>10/28</td>
                <td>11/1</td>
                <td>11/03</td>
                <td>11/4</td>
                <td>11/7</td>
                <td>11/10</td>
                <td>11/11</td>
                <td>11/15</td>
                <td>11/17</td>
              </tr>
              <tr>
                <th>Defensive YPP</th>
                <td>10/19</td>
                <td>10/21</td>
                <td>10/24</td>
                <td>10/26</td>
                <td>10/28</td>
                <td>11/1</td>
                <td>11/03</td>
                <td>11/4</td>
                <td>11/7</td>
                <td>11/10</td>
                <td>11/11</td>
                <td>11/15</td>
                <td>11/17</td>
              </tr>
              <tr>
                <th>Closing Spread</th>
                <td>10/19</td>
                <td>10/21</td>
                <td>10/24</td>
                <td>10/26</td>
                <td>10/28</td>
                <td>11/1</td>
                <td>11/03</td>
                <td>11/4</td>
                <td>11/7</td>
                <td>11/10</td>
                <td>11/11</td>
                <td>11/15</td>
                <td>11/17</td>
              </tr>
              <tr>
                <th>Total Points</th>
                <td>10/19</td>
                <td>10/21</td>
                <td>10/24</td>
                <td>10/26</td>
                <td>10/28</td>
                <td>11/1</td>
                <td>11/03</td>
                <td>11/4</td>
                <td>11/7</td>
                <td>11/10</td>
                <td>11/11</td>
                <td>11/15</td>
                <td>11/17</td>
              </tr>
              <tr>
                <th>Off Rating</th>
                <td>10/19</td>
                <td>10/21</td>
                <td>10/24</td>
                <td>10/26</td>
                <td>10/28</td>
                <td>11/1</td>
                <td>11/03</td>
                <td>11/4</td>
                <td>11/7</td>
                <td>11/10</td>
                <td>11/11</td>
                <td>11/15</td>
                <td>11/17</td>
              </tr>
              <tr>
                <th>Possessions</th>
                <td>10/19</td>
                <td>10/21</td>
                <td>10/24</td>
                <td>10/26</td>
                <td>10/28</td>
                <td>11/1</td>
                <td>11/03</td>
                <td>11/4</td>
                <td>11/7</td>
                <td>11/10</td>
                <td>11/11</td>
                <td>11/15</td>
                <td>11/17</td>
              </tr>
              <tr>
                <th>Closing Spread</th>
                <td>10/19</td>
                <td>10/21</td>
                <td>10/24</td>
                <td>10/26</td>
                <td>10/28</td>
                <td>11/1</td>
                <td>11/03</td>
                <td>11/4</td>
                <td>11/7</td>
                <td>11/10</td>
                <td>11/11</td>
                <td>11/15</td>
                <td>11/17</td>
              </tr>
              <tr>
                <th>ATS Result</th>
                <td>10/19</td>
                <td>10/21</td>
                <td>10/24</td>
                <td>10/26</td>
                <td>10/28</td>
                <td>11/1</td>
                <td>11/03</td>
                <td>11/4</td>
                <td>11/7</td>
                <td>11/10</td>
                <td>11/11</td>
                <td>11/15</td>
                <td>11/17</td>
              </tr>
              <tr>
                <th>Side Cover Margin</th>
                <td>10/19</td>
                <td>10/21</td>
                <td>10/24</td>
                <td>10/26</td>
                <td>10/28</td>
                <td>11/1</td>
                <td>11/03</td>
                <td>11/4</td>
                <td>11/7</td>
                <td>11/10</td>
                <td>11/11</td>
                <td>11/15</td>
                <td>11/17</td>
              </tr>
              <tr>
                <th>Closing Total</th>
                <td>10/19</td>
                <td>10/21</td>
                <td>10/24</td>
                <td>10/26</td>
                <td>10/28</td>
                <td>11/1</td>
                <td>11/03</td>
                <td>11/4</td>
                <td>11/7</td>
                <td>11/10</td>
                <td>11/11</td>
                <td>11/15</td>
                <td>11/17</td>
              </tr>
              <tr>
                <th>O/U Result</th>
                <td>10/19</td>
                <td>10/21</td>
                <td>10/24</td>
                <td>10/26</td>
                <td>10/28</td>
                <td>11/1</td>
                <td>11/03</td>
                <td>11/4</td>
                <td>11/7</td>
                <td>11/10</td>
                <td>11/11</td>
                <td>11/15</td>
                <td>11/17</td>
              </tr>
              <tr>
                <th>Total Cover Margin</th>
                <td>10/19</td>
                <td>10/21</td>
                <td>10/24</td>
                <td>10/26</td>
                <td>10/28</td>
                <td>11/1</td>
                <td>11/03</td>
                <td>11/4</td>
                <td>11/7</td>
                <td>11/10</td>
                <td>11/11</td>
                <td>11/15</td>
                <td>11/17</td>
              </tr>
            </table>  -->
          </div>
          <div id="part_1">
           <!--  <table class="table table-borderless mb-0">
              <tr>
                <td>11/19</td>
                <td>11/21</td>
                <td>11/22</td>
                <td>11/24</td>
                <td>11/26</td>
                <td>11/28</td>
                <td>11/30</td>
                <td>12/1</td>
                <td>12/4</td>
                <td>12/6</td>
                <td>12/8</td>
                <td>12/9</td>
                <td>12/11</td>
                <th>Date</th>
              </tr>
              <tr>
                <td>11/19</td>
                <td>11/21</td>
                <td>11/22</td>
                <td>11/24</td>
                <td>11/26</td>
                <td>11/28</td>
                <td>11/30</td>
                <td>12/1</td>
                <td>12/4</td>
                <td>12/6</td>
                <td>12/8</td>
                <td>12/9</td>
                <td>12/11</td>
                <th>Opponent</th>
              </tr>
              <tr>
                <td>11/19</td>
                <td>11/21</td>
                <td>11/22</td>
                <td>11/24</td>
                <td>11/26</td>
                <td>11/28</td>
                <td>11/30</td>
                <td>12/1</td>
                <td>12/4</td>
                <td>12/6</td>
                <td>12/8</td>
                <td>12/9</td>
                <td>12/11</td>
                <th>Result</th>
              </tr>
              <tr>
                <td>11/19</td>
                <td>11/21</td>
                <td>11/22</td>
                <td>11/24</td>
                <td>11/26</td>
                <td>11/28</td>
                <td>11/30</td>
                <td>12/1</td>
                <td>12/4</td>
                <td>12/6</td>
                <td>12/8</td>
                <td>12/9</td>
                <td>12/11</td>
                <th>Overtime</th>
              </tr>
              <tr>
                <td>11/19</td>
                <td>11/21</td>
                <td>11/22</td>
                <td>11/24</td>
                <td>11/26</td>
                <td>11/28</td>
                <td>11/30</td>
                <td>12/1</td>
                <td>12/4</td>
                <td>12/6</td>
                <td>12/8</td>
                <td>12/9</td>
                <td>12/11</td>
                <th>Points</th>
              </tr>
              <tr>
                <td>11/19</td>
                <td>11/21</td>
                <td>11/22</td>
                <td>11/24</td>
                <td>11/26</td>
                <td>11/28</td>
                <td>11/30</td>
                <td>12/1</td>
                <td>12/4</td>
                <td>12/6</td>
                <td>12/8</td>
                <td>12/9</td>
                <td>12/11</td>
                <th>Opp Points</th>
              </tr>
              <tr>
                <td>11/19</td>
                <td>11/21</td>
                <td>11/22</td>
                <td>11/24</td>
                <td>11/26</td>
                <td>11/28</td>
                <td>11/30</td>
                <td>12/1</td>
                <td>12/4</td>
                <td>12/6</td>
                <td>12/8</td>
                <td>12/9</td>
                <td>12/11</td>
                <th>Total Points</th>
              </tr>
              <tr>
                <td>11/19</td>
                <td>11/21</td>
                <td>11/22</td>
                <td>11/24</td>
                <td>11/26</td>
                <td>11/28</td>
                <td>11/30</td>
                <td>12/1</td>
                <td>12/4</td>
                <td>12/6</td>
                <td>12/8</td>
                <td>12/9</td>
                <td>12/11</td>
                <th>Off Rating</th>
              </tr>
              <tr>
                <td>11/19</td>
                <td>11/21</td>
                <td>11/22</td>
                <td>11/24</td>
                <td>11/26</td>
                <td>11/28</td>
                <td>11/30</td>
                <td>12/1</td>
                <td>12/4</td>
                <td>12/6</td>
                <td>12/8</td>
                <td>12/9</td>
                <td>12/11</td>
                <th>Possessions</th>
              </tr>
              <tr>
                <td>11/19</td>
                <td>11/21</td>
                <td>11/22</td>
                <td>11/24</td>
                <td>11/26</td>
                <td>11/28</td>
                <td>11/30</td>
                <td>12/1</td>
                <td>12/4</td>
                <td>12/6</td>
                <td>12/8</td>
                <td>12/9</td>
                <td>12/11</td>
                <th>Closing Spreads</th>
              </tr>
              <tr>
                <td>11/19</td>
                <td>11/21</td>
                <td>11/22</td>
                <td>11/24</td>
                <td>11/26</td>
                <td>11/28</td>
                <td>11/30</td>
                <td>12/1</td>
                <td>12/4</td>
                <td>12/6</td>
                <td>12/8</td>
                <td>12/9</td>
                <td>12/11</td>
                <th>ATS Result</th>
              </tr>
              <tr>
                <td>11/19</td>
                <td>11/21</td>
                <td>11/22</td>
                <td>11/24</td>
                <td>11/26</td>
                <td>11/28</td>
                <td>11/30</td>
                <td>12/1</td>
                <td>12/4</td>
                <td>12/6</td>
                <td>12/8</td>
                <td>12/9</td>
                <td>12/11</td>
                <th>Side Cover Margin</th>
              </tr>
              <tr>
                <td>11/19</td>
                <td>11/21</td>
                <td>11/22</td>
                <td>11/24</td>
                <td>11/26</td>
                <td>11/28</td>
                <td>11/30</td>
                <td>12/1</td>
                <td>12/4</td>
                <td>12/6</td>
                <td>12/8</td>
                <td>12/9</td>
                <td>12/11</td>
                <th>Closing Total</th>
              </tr>
              <tr>
                <td>11/19</td>
                <td>11/21</td>
                <td>11/22</td>
                <td>11/24</td>
                <td>11/26</td>
                <td>11/28</td>
                <td>11/30</td>
                <td>12/1</td>
                <td>12/4</td>
                <td>12/6</td>
                <td>12/8</td>
                <td>12/9</td>
                <td>12/11</td>
                <th>O/U Result</th>
              </tr>
              <tr>
                <td>11/19</td>
                <td>11/21</td>
                <td>11/22</td>
                <td>11/24</td>
                <td>11/26</td>
                <td>11/28</td>
                <td>11/30</td>
                <td>12/1</td>
                <td>12/4</td>
                <td>12/6</td>
                <td>12/8</td>
                <td>12/9</td>
                <td>12/11</td>
                <th>Opp Points</th>
              </tr>
            </table> -->
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
                <th colspan="6" class="c-white">OFFENSIVE SNAPS</th>
                <th>Week 1</th>
                <th>Week 2</th>
                <th>Week 3</th>
                <th>Week 4</th>
                <th>Week 5</th>
                <th>Week 6</th>
              </tr>
            </thead>
            <tbody>
              <tr class="dark-blue c-white">
                <th >Pos</th>
                <th>Player</th>
                <th>2018 Snaps</th>
                <th>Total Snaps</th>
                <th>Avg Snaps</th>
                <th>LAC 'W 23-20</th>
                <th>DALL 14-23</th>
                <th>@BUF W 45-19</th>
                <th>OAK</th>
                <th>BYE</th>
                <th>NYG</th>
                <th>@LAC</th>
                
              </tr>
              <tr>
                <td>C</td>
                <td class="a">Robin Lopez</td>
                <td class="a">652</td>
                <td class="a">626</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
                
              </tr>
              <tr>
                <td>T</td>
                <td class="a">Robin Lopez</td>
                <td class="a">652</td>
                <td class="a">605</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
              
              </tr>
              <tr>
                <td>H</td>
                <td class="a">Robin Lopez</td>
                <td class="a">652</td>
                <td class="a">516</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
                
              </tr>
              <tr>
                <td>WR</td>
                <td class="a">Robin Lopez</td>
                <td class="a">652</td>
                <td class="a">57.3</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
               
              </tr>
              <tr>
                <td>QB</td>
                <td class="a">Robin Lopez</td>
                <td class="a">652</td>
                <td class="a">63.9</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
               
              </tr>
              <tr>
                <td>T</td>
                <td class="a">Robin Lopez</td>
                <td class="a">652</td>
                <td class="a">786</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
              </tr>
            <tfoot>
              <tr class="bg-dark-blue c-balck">
                <th></th>
                <th colspan="3">OFFENSIVE SNAPS MISSED</th>
                <th>56</th>
                <th></th>
                <th></th>
                <th>56</th>
                <th>56</th>
                <th>56</th>
                <th>41</th>
                <th>71</th>
              </tr>
            </tfoot>
              </tbody>
          </table>
        </div>
        <div class="table_width" id="tab_2" >
          <table class="table" id="myTable">
         <!-- <thead>
              <tr class="bg-dark-blue c-balck">
              <th>Week 7</th>
                <th>Week 8</th>
              	<th>Week 9</th>
                <th>Week 10</th>
                <th>Week 11</th>
                <th>Week 12</th>
                <th>Week 13</th>
                <th>Week 14</th>
                <th>Week 15</th>
                <th>Week 15</th>
                <th>Week 16</th>
              </tr>
            </thead> -->
            <tbody>
           <!-- <tr class="dark-blue c-white">
              <th>@KC</th>
              <th>@PHI</th>
              <th>NE</th>
              <th>CIN</th>
              <th >@OAK</th>
              <th>@MIA</th>
              <th>NYJ</th>
              <th>@IND</th>
              <th>@WAS</th>
              <th>KC</th>
              <th>@WAS</th>
              <th>KC</th>
           </tr>
               <tr>
                <td>C</td>
                <td class="a">Robin Lopez</td>
                <td class="a">652</td>
                <td class="a">626</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
                
              </tr>
              <tr>
                <td>T</td>
                <td class="a">Robin Lopez</td>
                <td class="a">652</td>
                <td class="a">605</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
              
              </tr>
              <tr>
                <td>H</td>
                <td class="a">Robin Lopez</td>
                <td class="a">652</td>
                <td class="a">516</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
                
              </tr>
              <tr>
                <td>WR</td>
                <td class="a">Robin Lopez</td>
                <td class="a">652</td>
                <td class="a">57.3</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
               
              </tr>
              <tr>
                <td>QB</td>
                <td class="a">Robin Lopez</td>
                <td class="a">652</td>
                <td class="a">63.9</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
               
              </tr>
              <tr>
                <td>T</td>
                <td class="a">Robin Lopez</td>
                <td class="a">652</td>
                <td class="a">786</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
                
              </tr>
            <tfoot>
              <tr class="bg-dark-blue c-balck">
                <th></th>
                <th colspan="3">OFFENSIVE SNAPS MISSED</th>
                <th>56</th>
                <th></th>
                <th></th>
                <th>56</th>
                <th>56</th>
                <th>56</th>
                <th>41</th>     
                <th>71</th>
              </tr>
            </tfoot> --> 
              </tbody>
          </table>
        </div>
         <div class="table_width" id="tab_21" >
          <table class="table" id="myTable1">
         <!-- <thead>
              <tr class="bg-dark-blue c-balck">
              <th>Week 7</th>
                <th>Week 8</th>
              	<th>Week 9</th>
                <th>Week 10</th>
                <th>Week 11</th>
                <th>Week 12</th>
                <th>Week 13</th>
                <th>Week 14</th>
                <th>Week 15</th>
                <th>Week 15</th>
                <th>Week 16</th>
              </tr>
            </thead> -->
            <tbody>
           <!-- <tr class="dark-blue c-white">
              <th>@KC</th>
              <th>@PHI</th>
              <th>NE</th>
              <th>CIN</th>
              <th >@OAK</th>
              <th>@MIA</th>
              <th>NYJ</th>
              <th>@IND</th>
              <th>@WAS</th>
              <th>KC</th>
              <th>@WAS</th>
              <th>KC</th>
           </tr>
               <tr>
                <td>C</td>
                <td class="a">Robin Lopez</td>
                <td class="a">652</td>
                <td class="a">626</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
                
              </tr>
              <tr>
                <td>T</td>
                <td class="a">Robin Lopez</td>
                <td class="a">652</td>
                <td class="a">605</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
              
              </tr>
              <tr>
                <td>H</td>
                <td class="a">Robin Lopez</td>
                <td class="a">652</td>
                <td class="a">516</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
                
              </tr>
              <tr>
                <td>WR</td>
                <td class="a">Robin Lopez</td>
                <td class="a">652</td>
                <td class="a">57.3</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
               
              </tr>
              <tr>
                <td>QB</td>
                <td class="a">Robin Lopez</td>
                <td class="a">652</td>
                <td class="a">63.9</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
               
              </tr>
              <tr>
                <td>T</td>
                <td class="a">Robin Lopez</td>
                <td class="a">652</td>
                <td class="a">786</td>
                <td class="a">30.0</td>
                <td class="a">104</td>
                <td>107</td>
                <td class="a">35</td>
                <td class="a">36</td>
                <td class="a">37</td>
                <td class="a">32</td>
                <td class="a">32</td>
                
              </tr>
            <tfoot>
              <tr class="bg-dark-blue c-balck">
                <th></th>
                <th colspan="3">OFFENSIVE SNAPS MISSED</th>
                <th>56</th>
                <th></th>
                <th></th>
                <th>56</th>
                <th>56</th>
                <th>56</th>
                <th>41</th>     
                <th>71</th>
              </tr>
            </tfoot> --> 
              </tbody>
          </table>
        </div>
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


  //$("#tab_1").hide();
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