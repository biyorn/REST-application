import Axios from 'axios'

export function authenticate(params) {
    return (
        Axios.post('http://localhost:8080/authenticate', params)
    )
}