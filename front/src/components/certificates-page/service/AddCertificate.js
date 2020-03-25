import Axios from 'axios'
import createTokenProvider from '../../login/token/CreateTokenProvider'

const tokenProvider = createTokenProvider();

export function addCertificate(certificate) {
    return (
        Axios.post('http://localhost:8080/certificates', {
            name: certificate.name,
            description: certificate.desc,
            price: certificate.price,
            durationDays: certificate.duration,
            active: true,
            tags: certificate.tags
        },
            {
                headers: {
                    'Authorization': tokenProvider.getAccessToken()
                }
            }
        )
    )
}