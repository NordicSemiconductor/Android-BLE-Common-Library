# Android BLE Common Library

This library is an addon to the [Android BLE Library](https://github.com/NordicSemiconductor/Android-BLE-Library) 
which provides data parsers and other useful features for some Bluetooth SIG Adopted profies.

The library is under development. It's compatible with the Android BLE Library version from the 
[develop](https://github.com/NordicSemiconductor/Android-BLE-Library/tree/develop) branch, and Android nRF Toolbox, also on 
[develop](https://github.com/NordicSemiconductor/Android-nRF-Toolbox/tree/develop) branch.

Both the BLE Library and BLE Common Library may change until they go out of alpha version, after that they will have 
version number 2.0. The current stable version of BLE Library is 1.2 (master branch).

## Services

Currently the following service have been implemented:

- Battery Service
- Date Time
- DST Offset
- Time Zone
- Blood Pressure
- Glucose (with Record Access Control Point)
- Continuous Glucose (with Record Access Control Point)
- Cycling Speed and Cadence
- Heart Rate
- Health Thermometer
- Running Speed and Cadence

Next coming are:

- Bond Manager

## Testing

All data parsers have unit tests and should work properly. The classes are used by nRF Toolbox and were tested against sample 
apps from Nordic SDK 15 on nRF 51 and nRF52 DKs.

## Note

In CGMS profile the E2E CRC bytes may be inverted. Unfortunately, I couldn't find any working device or test data in order 
to verify the implementation. The sample from the SDK does not support E2E CRC.

## Contribution

Feel free to add more services after the API of BLE Library becomes stable. The library is and will be based on BSD-3 License.
Any feedback is welcome.

Please, use Issues to report bugs or submit suggestions.
