import React from 'react'
import './Alert.css'
import { Alert } from 'react-bootstrap'

const ErrorAlert = (props) => {
    return (
        <Alert variant="danger" className="bad-alert" onClose={props.close} dismissible>
            <Alert.Heading>{props.message}</Alert.Heading>
        </Alert>
    );
}

export default ErrorAlert;