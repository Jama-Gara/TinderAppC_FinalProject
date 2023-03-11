// Get all the remove buttons and attach a click event listener to each one
var removeButtons = document.getElementsByClassName("favourites-card-remove");
for (var i = 0; i < removeButtons.length; i++) {
    removeButtons[i].addEventListener("click", function(event) {
        event.preventDefault(); // Prevent the default anchor tag behavior
        var card = this.parentNode; // Get the parent card element
        var userId = card.querySelector(".favourites-card-title").getAttribute("data-user-id");
        var status = card.querySelector(".favourites-card-description span").textContent.toLowerCase();
        // Make an AJAX request to the remove endpoint
        fetch("/favorites/remove?userId=" + userId + "&status=" + status, {
            method: "POST",
            credentials: "same-origin"
        })
            .then(function(response) {
                if (response.ok) {
                    // If the request was successful, remove the card from the UI
                    card.parentNode.removeChild(card);
                } else {
                    // If there was an error, show an error message
                    alert(response.statusText);
                }
            })
            .catch(function(error) {
                // If there was a network error, show an error message
                alert("Network error: " + error.message);
            });
    });
}
