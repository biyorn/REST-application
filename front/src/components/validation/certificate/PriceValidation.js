export function verifyPrice(price) {
    return /^[0-9]+([,.][0-9]{1,2})?$/.test(price) && price > 0 && price < 1000000;
}