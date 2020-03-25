import { describe, it } from 'mocha';
import { verifyLogin } from '../components/validation/user/LoginValidation';
import { expect } from 'chai';

describe('verifyLogin', () => {
    it('Login is valid', () => {
        expect(verifyLogin('valid')).to.be.true;
    })
    it('Login is invalid', () => {
        expect(verifyLogin('..invalid')).to.be.false;
    })
})