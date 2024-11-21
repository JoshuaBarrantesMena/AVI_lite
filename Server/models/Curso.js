// models/Curso.js
const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

const Curso = sequelize.define('curso', {
    curso_id: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true, 
        allowNull: false,
    },
    carrera_id: {
        type: DataTypes.INTEGER,
        allowNull: false,
        references: {
            model: 'carreras', 
            key: 'carrera_id'
        }
    },
    profesor_id: {
        type: DataTypes.INTEGER,
        allowNull: false,
        references: {
            model: 'usuario', 
            key: 'identificacion'
        }
    },
    nombre: {
        type: DataTypes.STRING(100),
        allowNull: false,
    },
    informacion: {
        type: DataTypes.STRING(400), 
        allowNull: true,
    },
    campos_totales: {
        type: DataTypes.INTEGER.UNSIGNED.ZEROFILL, 
        allowNull: true,
    },
}, {
    tableName: 'cursos', 
    timestamps: false, 
});

module.exports = Curso;