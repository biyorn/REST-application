import React from 'react'
import { DropdownButton, Dropdown } from 'react-bootstrap'
import locale from '../../header/language/Localization'

const PageSizeDropdown = (props) => {
    const pageSize = props.pageSize;
    return (
        <DropdownButton variant='primary' title={locale.getString('size')}>
            <Dropdown.Item as="button" value="5" active={pageSize == 5} onClick={props.changePageSize}>5</Dropdown.Item>
            <Dropdown.Item as="button" value="25" active={pageSize == 25} onClick={props.changePageSize}>25</Dropdown.Item>
            <Dropdown.Item as="button" value="50" active={pageSize == 50} onClick={props.changePageSize}>50</Dropdown.Item>
            <Dropdown.Item as="button" value="100" active={pageSize == 100} onClick={props.changePageSize}>100</Dropdown.Item>
        </DropdownButton>
    )
}

export default PageSizeDropdown;