let currentIcon;

const getUserIcon = () => currentIcon;

// permet de set une icone pour l'user dans la navbar
const setUserIcon = (icon) => {
    currentIcon = icon;
};

const clearUserIcon = () => {
    currentIcon = undefined;
};

export { getUserIcon, setUserIcon, clearUserIcon };