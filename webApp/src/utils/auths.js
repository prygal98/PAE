const STORE_NAME = 'user';
const TOKEN = 'token';
const REMEMBER_ME = 'remembered';

let currentUser;
let token;

const getRememberMe =() =>{
  const rememberedSerialized = localStorage.getItem(REMEMBER_ME);
  const remembered = JSON.parse(rememberedSerialized);
  return remembered;
}

const getCurrentToken =()=>{
  if(token !== undefined){
    return token;
  }

  const remembered = getRememberMe();
  const serializedToken = remembered
      ? localStorage.getItem(TOKEN)
      : sessionStorage.getItem(TOKEN);

  if (!serializedToken) {
    return undefined;
  }

  token = JSON.parse(serializedToken);
  return token;
}

const getAuthenticatedUser = () => {
  if (currentUser !== undefined) {
    return currentUser;
  }

  const remembered = getRememberMe();
  const serializedUser = remembered
      ? localStorage.getItem(STORE_NAME)
      : sessionStorage.getItem(STORE_NAME);

  if (!serializedUser) {
    return undefined;
  }

  currentUser = JSON.parse(serializedUser);
  return currentUser;
};

const setAuthenticatedUser = (authenticatedUser) => {
  const serializedUser = JSON.stringify(authenticatedUser.user);
  const serializedToken = JSON.stringify(authenticatedUser.token);
  const remembered = getRememberMe();
  if (remembered) {
    localStorage.setItem(STORE_NAME, serializedUser);
    localStorage.setItem(TOKEN, serializedToken);
  } else {
    sessionStorage.setItem(STORE_NAME, serializedUser);
    sessionStorage.setItem(TOKEN,serializedToken);
  }
  currentUser = authenticatedUser.user;
};

const isAuthenticated = () => currentUser !== undefined;

const clearAuthenticatedUser = () => {
  localStorage.clear();
  sessionStorage.clear();
  currentUser = undefined;
  token = undefined;
}

function setRememberMe(remembered) {
  const rememberedSerialized = JSON.stringify(remembered);
  localStorage.setItem(REMEMBER_ME, rememberedSerialized);
}

export {
  getAuthenticatedUser,
  setAuthenticatedUser,
  isAuthenticated,
  clearAuthenticatedUser,
  getRememberMe,
  setRememberMe,
    getCurrentToken
};
