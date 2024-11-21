// models/Asignacion.js
const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

const Asignacion = sequelize.define('asignacion', {
    id_asignacion: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
    },
    titulo: {
        type: DataTypes.STRING(80),
        allowNull: true, 
    },
    id_profesor: {
        type: DataTypes.INTEGER,
        allowNull: false,
        references: {
            model: 'usuario', 
            key: 'identificacion'
        }
    },
    id_curso: {
        type: DataTypes.INTEGER,
        allowNull: false,
        references: {
            model: 'cursos', 
            key: 'curso_id'
        }
    },
    valor_porcentual: {
        type: DataTypes.INTEGER,
        allowNull: true,
    },
    fecha_limite: {
        type: DataTypes.DATE,
        allowNull: true,
    },
    descripcion: {
        type: DataTypes.STRING(300),
        allowNull: true,
    }
}, {
    tableName: 'asignaciones',
    timestamps: false,
});

module.exports = Asignacion;