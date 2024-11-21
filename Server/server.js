const express = require('express');
const cors = require('cors');
const sequelize = require('./config/database');
const dotenv = require('dotenv');

dotenv.config(); 

const app = express();
const PORT = process.env.PORT;
const bodyParser = require('body-parser');


app.use(bodyParser.json());
app.use(cors());
app.use(express.json()); 


const authRoutes = require('./routes/authRoutes');
const universityRoute = require('./routes/universityRoute');
const facultadRoutes = require('./routes/facultadRoutes');
const departamentoRoutes = require('./routes/departamentoRoutes');
const carreraRoutes = require('./routes/carreraRoutes');
const cursoRoutes = require('./routes/cursoRoutes');
const asignacionRoutes = require('./routes/asignacionRoutes');
const cursoUsuarioRoutes = require('./routes/cursoUsuarioRoutes');
const documentoRoutes = require('./routes/documentoRoutes');
const iaRoutes = require('./routes/iaRoutes');
const iaDocumentoRoutes = require('./routes/iaDocumentoRoutes');
const listaAsignacionRoutes = require('./routes/listaAsignacionRoutes');


app.use('/api/auth', authRoutes);
app.use('/api/universidades', universityRoute);
app.use('/api/facultades', facultadRoutes);
app.use('/api/departamentos', departamentoRoutes);
app.use('/api/carreras', carreraRoutes);
app.use('/api/cursos', cursoRoutes);
app.use('/api/asignaciones', asignacionRoutes);
app.use('/api/inscripciones', cursoUsuarioRoutes);
app.use('/api/documentos', documentoRoutes);
app.use('/api/ia', iaRoutes);
app.use('/api/ia-documento', iaDocumentoRoutes);
app.use('/api/lista-asignaciones', listaAsignacionRoutes);


sequelize.sync().then(() => {
    app.listen(PORT, () => {
        console.log("Servidor corriendo en el puerto" ,PORT);
    });
}).catch(err => {
    console.error('Error al sincronizar la base de datos:', err);
});


app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).send('Algo salió mal!');
});