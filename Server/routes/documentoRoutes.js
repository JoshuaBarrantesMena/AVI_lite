
const express = require('express');
const documentoController = require('../controllers/documentoController');
const authMiddleware = require('../middlewares/authMiddleware'); 

const router = express.Router();


router.post('/', authMiddleware, documentoController.subirDocumento);

router.get('/', authMiddleware, documentoController.getAllDocumentos);

router.get('/:id', authMiddleware, documentoController.getDocumentoById);

router.put('/:id', authMiddleware, documentoController.updateDocumento);

router.delete('/:id', authMiddleware, documentoController.deleteDocumento);

module.exports = router;
