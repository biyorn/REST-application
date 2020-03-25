import Axios from 'axios'
import createTokenProvider from '../../login/token/CreateTokenProvider'

const tokenProvider = createTokenProvider();

export function getUserCertificates(params) {
    const pageSize = params.pageSize;
    const pageNum = params.pageNum ? params.pageNum : 1;
    return (
        Axios.get('http://localhost:8080/certificates/user', {
            params: {
                pageNum: pageNum,
                pageSize: pageSize
            },
            headers: {
                'Authorization': tokenProvider.getAccessToken()
            }
        })
    )
}