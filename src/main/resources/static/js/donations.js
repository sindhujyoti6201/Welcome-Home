// Track used locations
let usedLocations = new Set();

const API_ENDPOINTS = {
    CATEGORIES: '/api/categories',
    SUBCATEGORIES: '/api/subcategories',
    CHECK_LOCATION: '/api/check-location',
    SUBMIT_DONATION: '/api/donations'
};

// Load staff name
async function loadStaffName() {
    try {
        var donatedByElement = document.getElementById("donatedBy");
        var username = donatedByElement.getAttribute("data-username");
        document.getElementById('staffName').textContent = username || 'Staff';
    } catch (error) {
        console.error('Failed to load staff name:', error);
        document.getElementById('staffName').textContent = 'Staff';
    }
}

// Load categories from database
async function loadCategories() {
    try {
        const response = await fetch(API_ENDPOINTS.CATEGORIES);
        const categories = await response.json();

        const select = document.querySelector('select[name="mainCategory"]');
        select.innerHTML = '<option value="">Select Category</option>';

        categories.forEach(category => {
            const option = new Option(category, category);
            select.add(option);
        });
    } catch (error) {
        console.error('Failed to load categories:', error);
    }
}

// Load subcategories based on selected category
async function loadSubcategories() {
    const mainCategory = document.querySelector('select[name="mainCategory"]').value;

    try {
        const response = await fetch(`${API_ENDPOINTS.SUBCATEGORIES}/${mainCategory}`);
        const subcategories = await response.json();

        const select = document.querySelector('select[name="subCategory"]');
        select.innerHTML = '<option value="">Select Sub Category</option>';

        subcategories.forEach(subcat => {
            const option = new Option(subcat, subcat);
            select.add(option);
        });
    } catch (error) {
        console.error('Failed to load subcategories:', error);
    }
}

// Image preview functionality
function previewImage(input) {
    const preview = document.getElementById('imagePreview');
    preview.innerHTML = '';

    if (input.files && input.files[0]) {
        const file = input.files[0];

        // Validate file size (5MB max)
        if (file.size > 5 * 1024 * 1024) {
            alert('Image must be less than 5MB');
            input.value = '';
            preview.innerHTML = '<span class="placeholder-text">No image selected</span>';
            return;
        }

        const reader = new FileReader();
        reader.onload = function (e) {
            preview.innerHTML = `
                <img src="${e.target.result}" alt="Item preview">
                <button type="button" class="button remove-image" onclick="removeImage()">
                    <i class="fas fa-times"></i>
                </button>
            `;
        };
        reader.readAsDataURL(file);
    } else {
        preview.innerHTML = '<span class="placeholder-text">No image selected</span>';
    }
}

function removeImage() {
    document.getElementById('itemImage').value = '';
    document.getElementById('imagePreview').innerHTML =
        '<span class="placeholder-text">No image selected</span>';
}

function togglePiecesSection() {
    const hasPieces = document.getElementById('hasPieces').checked;
    const piecesSection = document.getElementById('piecesSection');

    piecesSection.style.display = hasPieces ? 'block' : 'none';

    if (hasPieces && !document.querySelector('.piece-container')) {
        addPieceForm();
    }
}

function addPieceForm() {
    const container = document.getElementById('piecesContainer');
    const index = container.children.length;

    const pieceForm = document.createElement('div');
    pieceForm.innerHTML = createPieceFormHTML(index);
    container.appendChild(pieceForm);
}

function createPieceFormHTML(index) {
    return `
        <div class="piece-container" id="piece-${index}">
            <h4>Piece #${index + 1}</h4>
            
            <div class="form-group">
                <label>Description</label>
                <input type="text" name="piece_description_${index}" required>
            </div>

            <div class="form-group">
                <label>Dimensions (inches)</label>
                <div class="grid-3">
                    <input type="number" name="piece_length_${index}" placeholder="Length" required min="1">
                    <input type="number" name="piece_width_${index}" placeholder="Width" required min="1">
                    <input type="number" name="piece_height_${index}" placeholder="Height" required min="1">
                </div>
            </div>

            <div class="form-group">
                <label>Location *</label>
                <div class="grid-2">
                    <input type="number" 
                           name="piece_room_${index}" 
                           placeholder="Room Number" 
                           required 
                           min="1">
                    <input type="number" 
                           name="piece_shelf_${index}" 
                           placeholder="Shelf Number" 
                           required 
                           min="1">
                </div>
            </div>

            <div class="form-group">
                <label>Notes</label>
                <textarea name="piece_notes_${index}"></textarea>
            </div>

            ${index > 0 ? `
                <button type="button" class="button" onclick="removePiece(${index})">
                    Remove Piece
                </button>
            ` : ''}
        </div>
    `;
}

async function handleSubmit(event) {
    event.preventDefault();

    const formData = new FormData(event.target);

    const donorName = sessionStorage.getItem('username');
    const donateDate = new Date().toISOString().split('T')[0];
    const itemDescription = formData.get("itemDescription");
    const color = formData.get("color");
    const material = formData.get("material");
    const mainCategory = formData.get("mainCategory");
    const subCategory = formData.get("subCategory");
    const isNew = formData.get("isNew") === "on";
    const hasPieces = formData.get("hasPieces") === "on";

    // Collect pieces data if hasPieces is true
    let pieces = [];
    if (hasPieces) {
        pieces = Array.from(document.querySelectorAll('.piece-container')).map((pieceContainer, index) => ({
            pieceNum: index + 1,  // Set piece number (1-based index)
            pieceDescription: pieceContainer.querySelector(`input[name="piece_description_${index}"]`).value,
            length: parseFloat(pieceContainer.querySelector(`input[name="piece_length_${index}"]`).value),
            width: parseFloat(pieceContainer.querySelector(`input[name="piece_width_${index}"]`).value),
            height: parseFloat(pieceContainer.querySelector(`input[name="piece_height_${index}"]`).value),
            roomNum: pieceContainer.querySelector(`input[name="piece_room_${index}"]`).value,
            shelfNum: pieceContainer.querySelector(`input[name="piece_shelf_${index}"]`).value,
            pNotes: pieceContainer.querySelector(`textarea[name="piece_notes_${index}"]`).value,
        }));
    }

    // Prepare image as Base64
    let itemImage = null;
    const fileInput = document.getElementById('itemImage');
    if (fileInput.files[0]) {
        itemImage = await toBase64(fileInput.files[0]);
    }

    // Construct DonationRequest JSON
    const donationRequest = {
        donorName,
        donateDate,
        itemDescription,
        color,
        material,
        mainCategory,
        subCategory,
        isNew,
        hasPieces,
        pieces,
        itemImage,
    };

    try {
        const response = await fetch(API_ENDPOINTS.SUBMIT_DONATION, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(donationRequest),
        });

        if (response.ok) {
            alert('Donation recorded successfully!');
        } else {
            alert('Failed to record donation.');
        }
    } catch (error) {
        console.error('Error submitting donation:', error);
        alert('Error submitting donation');
    }
}

document.addEventListener('DOMContentLoaded', function () {
    console.log("Calling categories");
    loadStaffName();
    loadCategories();  // Populate categories on page load
});

// Helper function to convert file to Base64
function toBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = () => resolve(reader.result.split(',')[1]); // Exclude the prefix
        reader.onerror = (error) => reject(error);
        reader.readAsDataURL(file);
    });
}