import Axios from 'axios'
import createTokenProvider from '../../login/token/CreateTokenProvider'

const tokenProvider = createTokenProvider();

export function updateCertificate(certificate) {
    return (
        Axios.put('http://localhost:8080/certificates/' + certificate.id, {
            name: certificate.name,
            description: certificate.desc,
            price: certificate.price,
            durationDays: certificate.duration,
            active: certificate.active,
            tags: certificate.tags.map(tag => {
                return {
                    title: tag.title
                }
            })
        },
            {
                headers: {
                    'Authorization': tokenProvider.getAccessToken()
                }
            })
    )
}