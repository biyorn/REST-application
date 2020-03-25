import Axios from 'axios'
import createTokenProvider from '../../login/token/CreateTokenProvider'

const tokenProvider = createTokenProvider();

export function deleteCertificate(id) {
    return (
        Axios.delete('http://localhost:8080/certificates/' + id,
            {
                headers: {
                    'Authorization': tokenProvider.getAccessToken()
                }
            })
    )
}