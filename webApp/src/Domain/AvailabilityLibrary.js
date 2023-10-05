import {getCurrentToken} from "../utils/auths";
import {renderPopUp} from "../utils/utilsForm";
import {clearActive} from "../utils/activeLink";
import Navigate from "../Components/Router/Navigate";

async function getAvailabilities() {
    let availabilities;
    try {
        const options = {
            method: 'GET', // *GET, POST, PUT, DELETE, etc.
            headers: {
                'Content-Type': 'application/json',
            },
        };

        const reponse = await fetch(`http://localhost:8080/availability/getAvailabilitiesList`, options);

        if (!reponse.ok) {
            throw new Error(`fetch error : ${reponse.status}${reponse.statusText}`);
        }

        availabilities = await reponse.json();
    } catch (err) {
        console.error('error: ', err);
    }
    return availabilities;
}

async function addAvailabilities() {
    const currentToken = getCurrentToken();
    const token = `Bearer ${currentToken}`;
    console.log('tokennn', token);

    const annee = document.getElementById('annee').value;
    const mois = document.getElementById('mois').value;
    const jour = document.getElementById('jour').value;
    const timeSlot = document.getElementById('timeSlotChoix').value;

    console.log('trucs de l availability', +annee, +mois, +jour, timeSlot);

    const message = document.getElementById('message');
    if (annee.length === 0 || mois.length === 0 || jour.length === 0 || timeSlot ===0) {
        message.innerHTML = `<div id="snackbar">Veuillez compléter le formulaire!</div>`;
        renderPopUp();
    }
    const newData = {
        date: `${annee}-${mois}-${jour}T00:00:00`,
        time_slot:timeSlot
    };

    console.log('truc newdata', newData);

    try {
        const options = {
            method: 'POST', // *GET, POST, PUT, DELETE, etc.
            body: JSON.stringify(newData),
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token,
            },
        };

        const reponse = await fetch('http://localhost:8080/availability/add', options);

        if (!reponse.ok) {
            message.innerHTML = `<div id="snackbar">La disponibilité n'as pas été ajoutée!</div>`;
            renderPopUp();
            throw new Error(
                // eslint-disable-next-line no-irregular-whitespace
                `fetch error : ${reponse.status} : ${reponse.statusText}`,
            );
        }
        message.innerHTML = `<div id="snackbar">La disponibilité à bien été ajoutée!</div>`;
        renderPopUp();
        clearActive();

        Navigate('/profile');

    } catch
        (err) {
        // eslint-disable-next-line
        console.error('error: ', err);
    }
}
export {getAvailabilities, addAvailabilities}