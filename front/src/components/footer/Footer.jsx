import React from 'react'
import './Footer.css'
import { Navbar } from 'react-bootstrap'

const Footer = () => (
    <Navbar bg="primary" variant="dark" fixed="bottom" className="Footer">
        <Navbar.Brand className="ml-auto mr-auto">
            <p className="pb-0 mb-0">Certificates © 2020</p>
        </Navbar.Brand>
    </Navbar>
)

export default Footer;