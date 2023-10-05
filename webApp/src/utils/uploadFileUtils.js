const uploadUserPFP = (file) => {
  const formData = new FormData();
  formData.append('file', file.files[0]);
  const options = {
    method: 'POST',
    body: formData
  };
  fetch('http://localhost:8080/users/uploadPhoto', options);
  return false;
}

const setUserPFP = () => {
  const formData = new FormData();
  const options = {
    method: 'POST',
    body: formData
  };
  fetch('http://localhost:8080/users/setPhoto', options)
  return false;
}

const uploadObjectPhoto = (file) => {
  const formData = new FormData();
  formData.append('file', file.files[0]);
  const options = {
    method: 'POST',
    body: formData
  };
  fetch('http://localhost:8080/objects/uploadPhoto', options);
  return false;
}



export {uploadUserPFP, uploadObjectPhoto, setUserPFP};