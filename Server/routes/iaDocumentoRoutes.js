
const express = require('express');
const iaDocumentoController = require('../controllers/iaDocumentoController');
const authMiddleware = require('../middlewares/authMiddleware'); 

const router = express.Router();


router.post('/', authMiddleware, iaDocumentoController.asociarIaDocumento);

router.get('/', authMiddleware, iaDocumentoController.getAllIaDocumentos);

router.get('/ia/:id_ia', authMiddleware, iaDocumentoController.getDocumentosByIa);

router.get('/documento/:id_documento', authMiddleware, iaDocumentoController.getIaByDocumento);

router.delete('/:id_ia/:id_documento', authMiddleware, iaDocumentoController.eliminarIaDocumento);

module.exports = router;
