// models/Universidad.js
const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

const Universidad = sequelize.define('universidades', {
    universidad_id: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
    },
    nombre: {
        type: DataTypes.STRING(100),
        allowNull: false,
    },
}, {
    tableName: 'universidades',
    timestamps: false,
});

module.exports = Universidad;