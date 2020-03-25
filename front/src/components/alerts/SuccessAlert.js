import React from 'react'
import './Alert.css'
import { Alert } from 'react-bootstrap'

const SuccessAlert = (props) => {
    return (
        <Alert variant="success" className="success-alert" show={true} onClose={props.close} dismissible>
            <Alert.Heading>{props.message}</Alert.Heading>
        </Alert>
    );
}

export default SuccessAlert;