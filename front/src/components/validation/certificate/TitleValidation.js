export function verifyTitle(title) {
    return /^\w{2,25}$/.test(title);
}