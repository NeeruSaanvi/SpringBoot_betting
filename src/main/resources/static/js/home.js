
function callTeamConfrence(sportName){
	sessionStorage.setItem('sport', sportName);
	window.location.href="teamsconferences";
	//initializeUI(sportName);
}