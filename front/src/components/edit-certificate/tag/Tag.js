import React from 'react'
import { Container, Row, Col } from 'react-bootstrap'

class Tag extends React.Component {

    render() {
        return (
            <div className="tag">
                <Container>
                    <Row>
                        <Col sm={8}>
                            <p className="tag-title">
                                {this.props.title}
                            </p>
                        </Col>
                        <Col sm={4}>
                            <button type="button" value={this.props.title} onClick={this.props.handleDelete} className="tag-button">
                                x
                            </button>
                        </Col>
                    </Row>
                </Container>
            </div>
        )
    }
}

export default Tag;