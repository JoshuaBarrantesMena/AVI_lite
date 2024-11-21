// controllers/documentoController.js
const Documento = require('../models/Documento');



const crypto = require('crypto');
const path = require('path');

const MAX_PART_SIZE = 512 * 1024; // Tamaño máximo de cada parte: 512 KB

// Subir documento (simulado)
exports.subirDocumento = async (req, res) => {
    const { id_estudiante, url, hash_ } = req.body; // El cliente solo envía el path (url)

    if (!id_estudiante || !url || !hash_) {
        return res.status(400).json({ message: 'Faltan datos necesarios en la solicitud' });
    }

    try {
        // Simulación: dividir el "archivo" en partes
        const simulatedFile = Buffer.alloc(1024 * 1024, '0'); // Simula un archivo de 1MB en memoria
        const fileParts = [];
        for (let i = 0; i < simulatedFile.length; i += MAX_PART_SIZE) {
            fileParts.push(simulatedFile.slice(i, i + MAX_PART_SIZE));
        }

        // Simulación: calcular el hash de las partes combinadas
        const combinedBuffer = Buffer.concat(fileParts);
        const hashCalculado = crypto.createHash('sha256').update(combinedBuffer).digest('hex');

        // Validar el hash simulado contra el proporcionado
        if (hashCalculado !== hash_) {
            return res.status(400).json({ message: 'El hash del archivo no coincide' });
        }

        // Simulación: guardar metadatos en la base de datos
        const nuevoDocumento = await Documento.create({
            id_estudiante,
            url, // Ruta simulada enviada por el cliente
            hash_: hashCalculado,
        });

        res.status(201).json({ message: nuevoDocumento.id_documento });
    } catch (error) {
        console.error('Error al simular la subida del documento:', error);
        res.status(500).json({ message: 'Error al subir documento', error });
    }
};

// Obtener todos los documentos
exports.getAllDocumentos = async (req, res) => {
    try {
        const documentos = await Documento.findAll();
        res.json(documentos);
    } catch (error) {
        console.error('Error al obtener documentos:', error);
        res.status(500).json({ message: 'Error al obtener documentos', error });
    }
};

// Obtener documento por ID
exports.getDocumentoById = async (req, res) => {
    const { id } = req.params;

    try {
        const documento = await Documento.findOne({ where: { id_documento: id } });
        if (!documento) {
            return res.status(404).json({ message: 'Documento no encontrado' });
        }
        res.json(documento);
    } catch (error) {
        console.error('Error al obtener documento:', error);
        res.status(500).json({ message: 'Error al obtener documento', error });
    }
};


exports.deleteDocumento = async(req, res) => {
    const { id } = req.params;

    try {
        const documento = await Documento.findOne({ where: { id_documento: id } });
        if (!documento) {
            return res.status(404).json({ message: "Documento no encontrado" });
        }

        await documento.destroy(); // Eliminar el documento
        res.json({ message: "Documento eliminado con éxito" });
    } catch (error) {
        console.error("Error al eliminar documento:", error);
        res.status(500).json({ message: "Error al eliminar documento", error });
    }
};