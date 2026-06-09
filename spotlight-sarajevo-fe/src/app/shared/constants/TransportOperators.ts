export enum TransportOperator {
    GRAS = 1,
    CENTROTRANS = 2
}

// List of transport operators with their IDs and names. These are exact mirrors of the oprators from the database.
export const TRANSPORT_OPERATORS = [
    { id: 1, name: 'GRAS' },
    { id: 2, name: 'Centrotrans' }
];

// Enum for different transport types. These IDs correspond to the transport types in the database.
export enum TransportType {
    TRAMCAR = 1,
    TROLLEY = 2,
    BUS = 4,
    TAXI = 3
}
