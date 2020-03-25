import createTokenProvider from '../token/CreateTokenProvider'
import createBasketProvider from '../../certificates-page/basket/BasketProvider'

const createAuthProvider = () => {

    const tokenProvider = createTokenProvider();
    const basketProvider = createBasketProvider();

    const login = (newTokens) => {
        tokenProvider.setToken(newTokens);
    };

    const logout = () => {
        tokenProvider.deleteToken();
        basketProvider.clearBasket();
    };

    return {
        login,
        logout
    }
};

export default createAuthProvider;