let currentPage;

const getActiveLink = () => currentPage;

// permet de mettre cette page en actif dans la navbar
const setActiveLink = (page) => {
    currentPage = page;
};

const isActive = () => currentPage !== undefined;

const clearActive = () => {
    currentPage = undefined;
};

export { getActiveLink, setActiveLink, isActive, clearActive };