import * as React from 'react';
import Box from '@mui/material/Box';
import Collapse from '@mui/material/Collapse';
import IconButton from '@mui/material/IconButton';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import { Chip } from '@mui/material';
import { Stack } from '@mui/system';
import IContactResponse from '../../../types/contact/contactResponse.type';



function Row(props: { row: IContactResponse }) {
    const { row } = props;
    const [open, setOpen] = React.useState(false);
    const handleClick = () => {
        console.info('You clicked the Chip.');
    };

    return (
        <React.Fragment>
            <TableRow sx={{ '& > *': { borderBottom: 'unset' } }}>
                <TableCell>
                    <IconButton
                        aria-label="expand row"
                        size="small"
                        onClick={() => setOpen(!open)}
                    >
                        {open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
                    </IconButton>
                </TableCell>
                <TableCell component="th" scope="row">
                    {row.title}
                </TableCell>
                <TableCell align="right">{row.props.name}</TableCell>
                <TableCell align="right">{row.props.surname}</TableCell>
                <TableCell align="right">{row.props.address}</TableCell>
            </TableRow>
            <TableRow>
                <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
                    <Collapse in={open} timeout="auto" unmountOnExit>
                        <Box sx={{ margin: 1 }}>
                            <Typography variant="h6" gutterBottom component="div">
                                Attributes
                            </Typography>
                            <Stack direction="row" spacing={1}>
                                {row.attrs.map((attribute) => (
                                    <Chip label={attribute} onClick={handleClick} />

                                ))}
                            </Stack>
                        </Box>
                    </Collapse>
                </TableCell>
            </TableRow>
        </React.Fragment>
    );
}

interface TableProps {
    searchString: String,
    searchResults: Array<IContactResponse>
}

export default function SearchResultsTable(props: TableProps) {
    return (
        <TableContainer component={Paper}>
            <Table aria-label="collapsible table">
                <TableHead>
                    <TableRow>
                        <TableCell />
                        <TableCell><b>Title</b></TableCell>
                        <TableCell align="right"><b>Name</b></TableCell>
                        <TableCell align="right"><b>Surname</b></TableCell>
                        <TableCell align="right"><b>Address</b></TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {props.searchResults.map((contact) => (
                        <Row key={contact.uniqueId} row={contact} />
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
}