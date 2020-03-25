export function verifyLogin(login) {
    return /^[A-Za-z0-9]{5,30}$/.test(login);
}