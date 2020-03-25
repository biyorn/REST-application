export function verifyPassword(password) {
    return /^[A-Za-z0-9]{5,8}$/.test(password);
}