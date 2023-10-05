
function renderPopUp() {
    // Get the snackbar DIV
    const x = document.getElementById("snackbar");

    // Add the "show" class to DIV
    x.className = "show";

    // After 3 seconds, remove the show class from DIV
    setTimeout(()=> { x.className = x.className.replace("show", ""); }, 3000);
}

// eslint-disable-next-line import/prefer-default-export
export { renderPopUp }