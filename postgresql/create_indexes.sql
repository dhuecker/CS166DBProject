/* Booking dates are often compared using <= and >= in multiple options
therefore using a Btree should speed up the program in multiple areas.*/
CREATE INDEX bookingDateIndex
ON Booking
USING BTREE
(bookingDate);
/* Customer's first names and last names are also used in multiple functions,
usually checking if they're equal to a string. Therefore it will also be
beneficial to create indexes using hashes.*/
CREATE INDEX customerfNameIndex
ON Customer
USING HASH
(fname);

CREATE INDEX customerlNameIndex
ON Customer
USING HASH
(lname);