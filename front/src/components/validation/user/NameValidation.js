export function verifyName(name) {
    return /^[A-Z][a-z]{1,20}$/.test(name);
}