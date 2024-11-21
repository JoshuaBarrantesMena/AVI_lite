// models/Departamento.js
const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

const Departamento = sequelize.define('departamento', {
    departamento_id: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
    },
    facultad_id: {
        type: DataTypes.INTEGER,
        allowNull: false,
        references: {
            model: 'facultades',
            key: 'facultad_id'
        }
    },
    nombre: {
        type: DataTypes.STRING(100),
        allowNull: false,
    },
}, {
    tableName: 'departamentos',
    timestamps: false,
});

module.exports = Departamento;