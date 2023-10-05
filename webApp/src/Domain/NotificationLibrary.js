import Navigate from "../Components/Router/Navigate";
import {getCurrentToken} from "../utils/auths";

async function getNotifications() {
  const currentToken = getCurrentToken();
  const token = `Bearer ${currentToken}`;

  let notifications;
  try {
    const options = {
      method: 'GET', // *GET, POST, PUT, DELETE, etc.
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token,
      },
    };

    const reponse = await fetch(
        `http://localhost:8080/notification/getAllNotificationsProposed`,
        options);

    if (!reponse.ok) {
      throw new Error(`fetch error : ${reponse.status}${reponse.statusText}`);
    }

    notifications = await reponse.json();
  } catch (err) {
    console.error('error: ', err);
  }
  return notifications;
}

async function getMyNotifications(id) {
  const currentToken = getCurrentToken();
  const token = `Bearer ${currentToken}`;

  let notifications;
  
  try {
    const options = {
      method: 'GET', // *GET, POST, PUT, DELETE, etc.
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token,
      },
    };

    const reponse = await fetch(
        `http://localhost:8080/notification/getAllMyNotifications?id=${id}`,
        options);

    if (!reponse.ok) {
      throw new Error(`fetch error : ${reponse.status}${reponse.statusText}`);
    }

    notifications = await reponse.json();
  } catch (err) {
    console.error('error: ', err);
  }
  return notifications;
}

async function markAsRead(id) {
  const currentToken = getCurrentToken();
  const token = `Bearer ${currentToken}`;
  console.log('tokennn', token);
  const newData = {
    id,
  }
  try {
    const options = {
      method: 'POST', // *GET, POST, PUT, DELETE, etc.
      body: JSON.stringify(newData),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token,
      },
    };
    console.log("a : ", options);
    const reponse = await fetch(`http://localhost:8080/notification/markAsRead`,
        options);

    if (!reponse.ok) {
      throw new Error(
          `fetch error : ${reponse.status}${reponse.statusText}`);
    }
    const rep = await reponse.json();
    Navigate('/object?id=', rep.targetedObject);
  } catch
      (err) {
    // eslint-disable-next-line no-console
    console.error('error: ', err);
  }
}

export {getNotifications, markAsRead, getMyNotifications}