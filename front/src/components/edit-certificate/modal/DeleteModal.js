import React from 'react'
import './DeleteModal.css'
import { Modal, Row, Col, Container, Button } from 'react-bootstrap'
import { withRouter } from 'react-router-dom'
import locale from '../../header/language/Localization'

class DeleteModal extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            active: this.props.active,
        }
    }

    render() {
        return (
            <Modal show={this.state.active} onHide={this.props.handleClose}>
                <Modal.Body className="delete-body">
                    <p>
                        {this.props.question}
                    </p>
                </Modal.Body>
                <Modal.Footer>
                    <Container>
                        <Row>
                            <Col sm={5} />
                            <Col>
                                <button className="cancel-button" onClick={this.props.handleClose}>
                                    {locale.getString('deleteCancel')}
                                </button>
                            </Col>
                            <Col className="modal-close">
                                <Button variant="light" className="ok-button" onClick={this.props.handleDelete}>
                                    {locale.getString('deleteOk')}
                                </Button>
                            </Col>
                        </Row>
                    </Container>
                </Modal.Footer>
            </Modal>
        )
    }
}

export default withRouter(DeleteModal);