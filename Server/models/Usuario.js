const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');


const Usuario = sequelize.define('usuario', {
    identificacion: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        allowNull: false
    },
    pass: {
        type: DataTypes.STRING(32),
        allowNull: false
    },
    token: {
        type: DataTypes.STRING(32),
        allowNull: false
    },
    nombre: {
        type: DataTypes.STRING(45),
        allowNull: false
    },
    apellidos: {
        type: DataTypes.STRING(45),
        allowNull: false
    },
    correo: {
        type: DataTypes.STRING(45),
        allowNull: false
    },
    rol: {
        type: DataTypes.STRING(45),
        allowNull: false
    },
    universidad_id: {
        type: DataTypes.INTEGER,
        allowNull: true,
        references: {
            model: 'universidades', 
            key: 'universidad_id'
        }
    }
}, {
    tableName: 'usuario',
    timestamps: false
});

module.exports = Usuario;