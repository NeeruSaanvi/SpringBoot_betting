
         

   $(".filter").click(function(){
    $(".left_side_animate").toggleClass("main");
    $('.left_side_animate').toggleClass('animate');

  });


  $(".btn-right").click(function(){
    $(".btn-right").toggleClass("main");
   

  });
  
  $('#Date').on('change', function() {
  var value = $(this).val();
 
});
       
      $('#Games').on('change', function() {
  var value = $(this).val();
  //alert(value);
});   

$(document).ready(function(e) {
  
  //$("#customdate").hide();
//  
//  $(".custom-date").click(function(){
//    var value = $(this).val();  
//    if(value==="start"){
//      $("#customdate").show();
//      $("#games").hide();
//    }else{
//      $("#customdate").hide();
//      $("#games").show();
//    }
//  });
    //customdate
});



    