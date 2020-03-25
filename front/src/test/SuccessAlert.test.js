import React from 'react'
import { describe, it } from 'mocha';
import { expect } from 'chai';
import { JSDOM } from 'jsdom';
import SuccessAlert from '../components/alerts/SuccessAlert'
import { configure, shallow } from 'enzyme';
import Adapter from 'enzyme-adapter-react-16'
configure({ adapter: new Adapter() });

describe('verifySuccessAlert', () => {

  it('should get success alert component', () => {
    const wrapper = shallow(<SuccessAlert />)
    expect(wrapper.find('Alert')).to.have.length(1);
  })
})