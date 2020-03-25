import Axios from 'axios'
import jwt_decode from 'jwt-decode'

const createTokenProvider = () => {

    const REACT_TOKEN_AUTH = JSON.parse(localStorage.getItem('REACT_TOKEN_AUTH'));

    const getAccessToken = () => {
        const token = 'Bearer ' + REACT_TOKEN_AUTH.accessToken
        return token ? token : ''
    }

    const getRefreshToken = (accessToken) => {
        return (
            Axios.post('http://localhost:8080/token', {
                refreshToken: accessToken
            })
        )
    }

    const refreshToken = () => {
        if (REACT_TOKEN_AUTH == null) {
            return '';
        }
        const accessToken = REACT_TOKEN_AUTH.accessToken;
        const refreshToken = REACT_TOKEN_AUTH.refreshToken;
        const jwt = jwt_decode(accessToken);
        const now = Date.now() / 1000;
        const exp = jwt.exp;
        if (now > exp) {
            getRefreshToken(refreshToken)
                .then(response => {
                    setToken(response.data);
                }).catch(error => {
                    localStorage.removeItem('REACT_TOKEN_AUTH');
                })
        }
    }

    const isLoggedIn = () => {
        return REACT_TOKEN_AUTH
    };

    const deleteToken = () => {
        localStorage.removeItem('REACT_TOKEN_AUTH');
        window.location.reload();
    }

    const setToken = (token) => {
        if (token) {
            localStorage.setItem('REACT_TOKEN_AUTH', JSON.stringify(token));
        } else {
            localStorage.removeItem('REACT_TOKEN_AUTH');
        }
    };

    const getRoles = () => {
        if (REACT_TOKEN_AUTH == null) {
            return [];
        }
        const token = REACT_TOKEN_AUTH.accessToken;
        const jwt = jwt_decode(token);
        const roles = jwt.ROLE.map(role => role.authority)
        return roles;
    }

    const getName = () => {
        if (REACT_TOKEN_AUTH == null) {
            return '';
        }
        const token = REACT_TOKEN_AUTH.accessToken;
        const jwt = jwt_decode(token);
        return jwt.sub;
    }

    return {
        refreshToken,
        getAccessToken,
        isLoggedIn,
        setToken,
        deleteToken,
        getRoles,
        getName
    }
}

export default createTokenProvider;