// models/Carrera.js
const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

const Carrera = sequelize.define('carrera', {
    carrera_id: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true, 
        allowNull: false,
    },
    departamento_id: {
        type: DataTypes.INTEGER,
        allowNull: false,
        references: {
            model: 'departamentos', 
            key: 'departamento_id'
        }
    },
    nombre: {
        type: DataTypes.STRING(100),
        allowNull: false,
    },
}, {
    tableName: 'carreras', 
    timestamps: false, 
});

module.exports = Carrera;