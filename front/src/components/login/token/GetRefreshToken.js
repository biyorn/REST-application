import Axios from 'axios'

export function getRefreshToken(token) {
    return (
        Axios.post('http://localhost:8080/token', {
            refreshToken: token
        })
    )
}