export function verifyTagName(tagName) {
    return /^\w{1,20}$/.test(tagName)
}