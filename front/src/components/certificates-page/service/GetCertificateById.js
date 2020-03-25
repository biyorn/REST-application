import Axios from 'axios'

export function getCertificateById(id) {
    return (
        Axios.get('http://localhost:8080/certificates/' + id)
    )
}