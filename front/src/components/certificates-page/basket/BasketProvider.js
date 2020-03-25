import Axios from 'axios'
import createTokenProvider from '../../login/token/CreateTokenProvider'

const tokenProvider = createTokenProvider();

const basketProvider = () => {

    const getBasket = () => {
        return JSON.parse(localStorage.getItem('BASKET'));
    }

    const addToBasket = (certificate) => {
        let localBasket = getBasket();
        console.log(localBasket)
        if (localBasket === null) {
            localBasket = []
            localBasket.push(certificate);
            localStorage.setItem('BASKET', JSON.stringify(localBasket));
        } else {
            localBasket.push(certificate);
            localStorage.setItem('BASKET', JSON.stringify(localBasket));
        }
    }

    const getCertificates = () => {
        let localBasket = getBasket();
        if (localBasket === null) {
            localBasket = []
        }
        return localBasket;
    }

    const buy = (certificates) => {
        const listId = certificates.map(certificate => certificate.id);
        return (
            Axios.post('http://localhost:8080/orders', {
                certificates: listId
            },
                {
                    headers: {
                        'Authorization': tokenProvider.getAccessToken()
                    }
                }
            )
        )
    }

    const clearBasket = () => {
        localStorage.removeItem('BASKET')
    }

    return {
        addToBasket,
        getCertificates,
        buy,
        clearBasket
    }
};

export default basketProvider;