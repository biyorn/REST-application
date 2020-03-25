import Axios from 'axios'

function signUp(login, pass, name, surname) {
    return (
        Axios.post('http://localhost:8080/sign-up', {
            login: login,
            password: pass,
            firstName: name,
            lastName: surname
        })
    )
}

export default signUp;