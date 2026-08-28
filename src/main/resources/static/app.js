let AUTH_HEADER = sessionStorage.getItem('hms_auth') || null;
let editingMode = {
    departments: false,
    doctors: false,
    patients: false,
    appointments: false,
    prescriptions: false,
    billings: false
};

function showLoginView() {
    const loginView = document.getElementById('login-view');
    const portalView = document.getElementById('portal-view');
    if (loginView) loginView.classList.remove('d-none');
    if (portalView) portalView.classList.add('d-none');
    const loginErr = document.getElementById('login-error');
    if (loginErr) loginErr.classList.add('d-none');
    const form = document.getElementById('login-form');
    if (form) form.reset();
}

function showPortalView() {
    const loginView = document.getElementById('login-view');
    const portalView = document.getElementById('portal-view');
    if (loginView) loginView.classList.add('d-none');
    if (portalView) portalView.classList.remove('d-none');
    const user = sessionStorage.getItem('hms_user') || 'admin';
    const userBadge = document.getElementById('logged-user-name');
    if (userBadge) userBadge.innerText = user;
}

async function handleLogin(e) {
    e.preventDefault();
    const username = document.getElementById('login-username').value.trim();
    const password = document.getElementById('login-password').value.trim();
    const errorEl = document.getElementById('login-error');
    const btn = document.getElementById('login-btn');
    errorEl.classList.add('d-none');

    const authHeader = 'Basic ' + btoa(`${username}:${password}`);
    btn.disabled = true;
    btn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span> Signing In...';

    try {
        const response = await fetch('/doctor/viewAll', {
            headers: {
                'Authorization': authHeader,
                'Content-Type': 'application/json'
            }
        });

        if (response.status === 401 || response.status === 403) {
            throw new Error('Invalid username or password!');
        }

        AUTH_HEADER = authHeader;
        sessionStorage.setItem('hms_auth', authHeader);
        sessionStorage.setItem('hms_user', username);
        showPortalView();
        loadDashboard();
    } catch (err) {
        errorEl.innerText = err.message || 'Login failed. Please check credentials.';
        errorEl.classList.remove('d-none');
    } finally {
        btn.disabled = false;
        btn.innerHTML = '<i class="fa-solid fa-right-to-bracket me-1"></i> Sign In';
    }
}

function logout() {
    AUTH_HEADER = null;
    sessionStorage.removeItem('hms_auth');
    sessionStorage.removeItem('hms_user');
    showLoginView();
}

async function fetchAPI(url, options = {}) {
    if (!AUTH_HEADER) {
        showLoginView();
        throw new Error('Not authenticated');
    }
    options.headers = {
        ...options.headers,
        'Authorization': AUTH_HEADER,
        'Content-Type': 'application/json'
    };
    try {
        const response = await fetch(url, options);
        if (response.status === 401 || response.status === 403) {
            logout();
            throw new Error('Session expired or unauthorized. Please sign in again.');
        }
        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.message || 'Request failed with status ' + response.status);
        }
        return data;
    } catch (err) {
        if (AUTH_HEADER) showAlert(err.message, 'danger');
        throw err;
    }
}

function showAlert(message, type = 'success') {
    const placeholder = document.getElementById('alert-placeholder');
    placeholder.innerHTML = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
    setTimeout(() => {
        placeholder.innerHTML = '';
    }, 4000);
}

function showSection(sectionId) {
    document.querySelectorAll('.content-section').forEach(el => el.classList.add('d-none'));
    document.querySelectorAll('#sidebar ul li').forEach(el => el.classList.remove('active'));
    
    const activeSection = document.getElementById('section-' + sectionId);
    if(activeSection) activeSection.classList.remove('d-none');

    const activeNav = Array.from(document.querySelectorAll('#sidebar ul li a'))
        .find(a => a.getAttribute('onclick') && a.getAttribute('onclick').includes(sectionId));
    if(activeNav) activeNav.parentElement.classList.add('active');

    const titleMap = {
        'dashboard': 'Dashboard',
        'departments': 'Department Management',
        'doctors': 'Doctor Management',
        'patients': 'Patient Management',
        'appointments': 'Appointment Management',
        'prescriptions': 'Prescription Management',
        'billings': 'Billing Management'
    };
    document.getElementById('section-title').innerText = titleMap[sectionId] || 'Hospital Management System';

    if (sectionId === 'dashboard') loadDashboard();
    if (sectionId === 'departments') loadDepartments();
    if (sectionId === 'doctors') loadDoctors();
    if (sectionId === 'patients') loadPatients();
    if (sectionId === 'appointments') loadAppointments();
    if (sectionId === 'prescriptions') loadPrescriptions();
    if (sectionId === 'billings') loadBillings();
}

function openModal(modalId) {
    const modal = new bootstrap.Modal(document.getElementById(modalId));
    modal.show();
}

// ================= DASHBOARD =================
async function loadDashboard() {
    try {
        const [deps, docs, pats, appts, bills] = await Promise.allSettled([
            fetchAPI('/department/viewAll'),
            fetchAPI('/doctor/viewAll'),
            fetchAPI('/patient/viewAll'),
            fetchAPI('/appointment/viewAll'),
            fetchAPI('/billing/viewAll')
        ]);

        document.getElementById('count-departments').innerText = deps.status === 'fulfilled' && deps.value.data ? deps.value.data.length : 0;
        document.getElementById('count-doctors').innerText = docs.status === 'fulfilled' && docs.value.data ? docs.value.data.length : 0;
        document.getElementById('count-patients').innerText = pats.status === 'fulfilled' && pats.value.data ? pats.value.data.length : 0;
        document.getElementById('count-appointments').innerText = appts.status === 'fulfilled' && appts.value.data ? appts.value.data.length : 0;
        document.getElementById('count-billings').innerText = bills.status === 'fulfilled' && bills.value.data ? bills.value.data.length : 0;
    } catch (e) {
        console.error(e);
    }
}

// ================= DEPARTMENTS =================
async function loadDepartments() {
    try {
        const res = await fetchAPI('/department/viewAll');
        const tbody = document.querySelector('#table-departments tbody');
        tbody.innerHTML = '';
        if (res.data && res.data.length > 0) {
            res.data.forEach(d => {
                tbody.innerHTML += `
                    <tr>
                        <td>${d.id}</td>
                        <td><span class="badge badge-dept p-2">${d.departmentName}</span></td>
                        <td>${d.description}</td>
                        <td>
                            <button class="btn btn-sm btn-outline-primary me-1" onclick="editDepartment(${JSON.stringify(d).replace(/"/g, '&quot;')})"><i class="fa-solid fa-pen"></i></button>
                            <button class="btn btn-sm btn-outline-danger" onclick="deleteDepartment(${d.id})"><i class="fa-solid fa-trash"></i></button>
                        </td>
                    </tr>
                `;
            });
        } else {
            tbody.innerHTML = '<tr><td colspan="4" class="text-center text-muted">No departments found</td></tr>';
        }
    } catch (e) {}
}

function openDepartmentModal() {
    editingMode.departments = false;
    document.getElementById('deptModalTitle').innerText = 'Add Department';
    document.getElementById('formDepartment').reset();
    document.getElementById('dept-id').readOnly = false;
    openModal('modalDepartment');
}

function editDepartment(dept) {
    editingMode.departments = true;
    document.getElementById('deptModalTitle').innerText = 'Update Department';
    document.getElementById('dept-id').value = dept.id;
    document.getElementById('dept-id').readOnly = true;
    document.getElementById('dept-name').value = dept.departmentName;
    document.getElementById('dept-desc').value = dept.description;
    openModal('modalDepartment');
}

async function saveDepartment(e) {
    e.preventDefault();
    const payload = {
        id: parseInt(document.getElementById('dept-id').value),
        departmentName: document.getElementById('dept-name').value,
        description: document.getElementById('dept-desc').value
    };
    const method = editingMode.departments ? 'PUT' : 'POST';
    const url = editingMode.departments ? '/department/update' : '/department/save';

    try {
        await fetchAPI(url, { method, body: JSON.stringify(payload) });
        showAlert(editingMode.departments ? 'Department updated successfully!' : 'Department created successfully!');
        bootstrap.Modal.getInstance(document.getElementById('modalDepartment')).hide();
        document.getElementById('formDepartment').reset();
        document.getElementById('dept-id').readOnly = false;
        editingMode.departments = false;
        loadDepartments();
    } catch (err) {}
}

async function deleteDepartment(id) {
    if (!confirm('Are you sure you want to delete this department?')) return;
    try {
        await fetchAPI(`/department/delete/${id}`, { method: 'DELETE' });
        showAlert('Department deleted successfully!');
        loadDepartments();
    } catch (e) {}
}

// ================= DOCTORS =================
async function openDoctorModal() {
    editingMode.doctors = false;
    document.getElementById('doctorModalTitle').innerText = 'Add Doctor';
    document.getElementById('formDoctor').reset();
    document.getElementById('doc-id').readOnly = false;
    await populateDepartmentDropdown('doc-dept-select');
    openModal('modalDoctor');
}

async function populateDepartmentDropdown(elementId, selectedId = null) {
    const select = document.getElementById(elementId);
    select.innerHTML = '<option value="">Select Department</option>';
    try {
        const res = await fetchAPI('/department/viewAll');
        if (res.data) {
            res.data.forEach(d => {
                const opt = document.createElement('option');
                opt.value = d.id;
                opt.text = `${d.departmentName} (ID: ${d.id})`;
                if (selectedId && (selectedId == d.id || selectedId == d.departmentName)) opt.selected = true;
                select.appendChild(opt);
            });
        }
    } catch (e) {}
}

async function loadDoctors() {
    try {
        const res = await fetchAPI('/doctor/viewAll');
        renderDoctors(res.data);
    } catch (e) {}
}

function renderDoctors(doctors) {
    const tbody = document.querySelector('#table-doctors tbody');
    tbody.innerHTML = '';
    if (doctors && doctors.length > 0) {
        doctors.forEach(d => {
            const deptName = d.departmentName || (d.department ? d.department.departmentName : (d.departmentId ? `Dept #${d.departmentId}` : 'N/A'));
            const exp = (d.experience !== undefined && d.experience !== null) ? `${d.experience} yrs` : 'N/A';
            const fee = (d.consultationFee !== undefined && d.consultationFee !== null) ? `$${d.consultationFee}` : 'N/A';
            tbody.innerHTML += `
                <tr>
                    <td>${d.id}</td>
                    <td>${d.name || ''}</td>
                    <td>${d.email || ''}</td>
                    <td><span class="badge bg-info text-dark">${d.specialization || ''}</span></td>
                    <td>${exp}</td>
                    <td>${fee}</td>
                    <td><span class="badge badge-dept p-2">${deptName}</span></td>
                    <td>
                        <button class="btn btn-sm btn-outline-primary me-1" onclick="editDoctor(${JSON.stringify(d).replace(/"/g, '&quot;')})"><i class="fa-solid fa-pen"></i></button>
                        <button class="btn btn-sm btn-outline-danger" onclick="deleteDoctor(${d.id})"><i class="fa-solid fa-trash"></i></button>
                    </td>
                </tr>
            `;
        });
    } else {
        tbody.innerHTML = '<tr><td colspan="8" class="text-center text-muted">No doctors found</td></tr>';
    }
}

async function editDoctor(doc) {
    editingMode.doctors = true;
    document.getElementById('doctorModalTitle').innerText = 'Update Doctor';
    document.getElementById('doc-id').value = doc.id;
    document.getElementById('doc-id').readOnly = true;
    document.getElementById('doc-name').value = doc.name || '';
    document.getElementById('doc-email').value = doc.email || '';
    document.getElementById('doc-spec').value = doc.specialization || '';
    document.getElementById('doc-exp').value = (doc.experience !== undefined && doc.experience !== null) ? doc.experience : '';
    document.getElementById('doc-fee').value = (doc.consultationFee !== undefined && doc.consultationFee !== null) ? doc.consultationFee : '';
    const deptId = doc.departmentId || (doc.department ? doc.department.id : null);
    await populateDepartmentDropdown('doc-dept-select', deptId);
    openModal('modalDoctor');
}

async function saveDoctor(e) {
    e.preventDefault();
    const payload = {
        id: parseInt(document.getElementById('doc-id').value),
        name: document.getElementById('doc-name').value,
        email: document.getElementById('doc-email').value,
        specialization: document.getElementById('doc-spec').value,
        experience: parseInt(document.getElementById('doc-exp').value),
        consultationFee: parseFloat(document.getElementById('doc-fee').value),
        departmentId: parseInt(document.getElementById('doc-dept-select').value)
    };
    const method = editingMode.doctors ? 'PUT' : 'POST';
    const url = editingMode.doctors ? '/doctor/update' : '/doctor/save';

    try {
        await fetchAPI(url, { method, body: JSON.stringify(payload) });
        showAlert(editingMode.doctors ? 'Doctor updated successfully!' : 'Doctor added successfully!');
        bootstrap.Modal.getInstance(document.getElementById('modalDoctor')).hide();
        loadDoctors();
    } catch (err) {}
}

async function deleteDoctor(id) {
    if (!confirm('Are you sure you want to delete this doctor?')) return;
    try {
        await fetchAPI(`/doctor/delete/${id}`, { method: 'DELETE' });
        showAlert('Doctor deleted successfully!');
        loadDoctors();
    } catch (e) {}
}

async function searchDoctorByEmail() {
    const email = document.getElementById('doctor-search-input').value.trim();
    if (!email) return loadDoctors();
    try {
        const res = await fetchAPI(`/doctor/viewByEmail/${email}`);
        renderDoctors(res.data ? [res.data] : []);
    } catch (e) {}
}

async function searchDoctorByDept() {
    const dept = document.getElementById('doctor-search-input').value.trim();
    if (!dept) return loadDoctors();
    try {
        const res = await fetchAPI(`/doctor/viewByDepartment/${dept}`);
        renderDoctors(res.data || []);
    } catch (e) {}
}

// ================= PATIENTS =================
function openPatientModal() {
    editingMode.patients = false;
    document.getElementById('patientModalTitle').innerText = 'Register Patient';
    document.getElementById('formPatient').reset();
    document.getElementById('pat-id').readOnly = false;
    openModal('modalPatient');
}

async function loadPatients() {
    try {
        const res = await fetchAPI('/patient/viewAll');
        renderPatients(res.data);
    } catch (e) {}
}

function renderPatients(patients) {
    const tbody = document.querySelector('#table-patients tbody');
    tbody.innerHTML = '';
    if (patients && patients.length > 0) {
        patients.forEach(p => {
            tbody.innerHTML += `
                <tr>
                    <td>${p.id}</td>
                    <td>${p.name}</td>
                    <td>${p.email}</td>
                    <td>${p.phone}</td>
                    <td>${p.age}</td>
                    <td><span class="badge bg-secondary">${p.gender}</span></td>
                    <td>${p.address}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-primary me-1" onclick="editPatient(${JSON.stringify(p).replace(/"/g, '&quot;')})"><i class="fa-solid fa-pen"></i></button>
                        <button class="btn btn-sm btn-outline-danger" onclick="deletePatient(${p.id})"><i class="fa-solid fa-trash"></i></button>
                    </td>
                </tr>
            `;
        });
    } else {
        tbody.innerHTML = '<tr><td colspan="8" class="text-center text-muted">No patients found</td></tr>';
    }
}

function editPatient(p) {
    editingMode.patients = true;
    document.getElementById('patientModalTitle').innerText = 'Update Patient';
    document.getElementById('pat-id').value = p.id;
    document.getElementById('pat-id').readOnly = true;
    document.getElementById('pat-name').value = p.name;
    document.getElementById('pat-email').value = p.email;
    document.getElementById('pat-phone').value = p.phone;
    document.getElementById('pat-age').value = p.age;
    document.getElementById('pat-gender').value = p.gender;
    document.getElementById('pat-address').value = p.address;
    openModal('modalPatient');
}

async function savePatient(e) {
    e.preventDefault();
    const payload = {
        id: parseInt(document.getElementById('pat-id').value),
        name: document.getElementById('pat-name').value,
        email: document.getElementById('pat-email').value,
        phone: document.getElementById('pat-phone').value,
        age: parseInt(document.getElementById('pat-age').value),
        gender: document.getElementById('pat-gender').value,
        address: document.getElementById('pat-address').value
    };
    const method = editingMode.patients ? 'PUT' : 'POST';
    const url = editingMode.patients ? '/patient/update' : '/patient/save';

    try {
        await fetchAPI(url, { method, body: JSON.stringify(payload) });
        showAlert(editingMode.patients ? 'Patient updated successfully!' : 'Patient registered successfully!');
        bootstrap.Modal.getInstance(document.getElementById('modalPatient')).hide();
        document.getElementById('formPatient').reset();
        document.getElementById('pat-id').readOnly = false;
        editingMode.patients = false;
        loadPatients();
    } catch (err) {}
}

async function deletePatient(id) {
    if (!confirm('Are you sure you want to delete this patient?')) return;
    try {
        await fetchAPI(`/patient/delete/${id}`, { method: 'DELETE' });
        showAlert('Patient deleted successfully!');
        loadPatients();
    } catch (e) {}
}

async function searchPatientByName() {
    const name = document.getElementById('patient-search-name').value.trim();
    if (!name) return loadPatients();
    try {
        const res = await fetchAPI(`/patient/viewByName/${name}`);
        renderPatients(res.data || []);
    } catch (e) {}
}

// ================= APPOINTMENTS =================
async function openAppointmentModal() {
    editingMode.appointments = false;
    document.getElementById('apptModalTitle').innerText = 'Book Appointment';
    document.getElementById('formAppointment').reset();
    document.getElementById('appt-id').readOnly = false;
    await Promise.all([
        populatePatientDropdown('appt-pat-select'),
        populateDoctorDropdown('appt-doc-select')
    ]);
    openModal('modalAppointment');
}

async function populatePatientDropdown(elementId, selectedId = null) {
    const select = document.getElementById(elementId);
    select.innerHTML = '<option value="">Select Patient</option>';
    try {
        const res = await fetchAPI('/patient/viewAll');
        if (res.data) {
            res.data.forEach(p => {
                const opt = document.createElement('option');
                opt.value = p.id;
                opt.text = `${p.name} (ID: ${p.id})`;
                if (selectedId && selectedId === p.id) opt.selected = true;
                select.appendChild(opt);
            });
        }
    } catch (e) {}
}

async function populateDoctorDropdown(elementId, selectedId = null) {
    const select = document.getElementById(elementId);
    select.innerHTML = '<option value="">Select Doctor</option>';
    try {
        const res = await fetchAPI('/doctor/viewAll');
        if (res.data) {
            res.data.forEach(d => {
                const opt = document.createElement('option');
                opt.value = d.id;
                opt.text = `Dr. ${d.name} (${d.specialization} - ID: ${d.id})`;
                if (selectedId && selectedId === d.id) opt.selected = true;
                select.appendChild(opt);
            });
        }
    } catch (e) {}
}

async function loadAppointments() {
    try {
        const res = await fetchAPI('/appointment/viewAll');
        renderAppointments(res.data);
    } catch (e) {}
}

function renderAppointments(appts) {
    const tbody = document.querySelector('#table-appointments tbody');
    tbody.innerHTML = '';
    if (appts && appts.length > 0) {
        appts.forEach(a => {
            const patName = a.patient ? a.patient.name : `Patient ID: ${a.patientId || 'N/A'}`;
            const docName = a.doctor ? `Dr. ${a.doctor.name}` : `Doctor ID: ${a.doctorId || 'N/A'}`;
            tbody.innerHTML += `
                <tr>
                    <td>${a.id}</td>
                    <td>${a.appointmentDate || 'N/A'}</td>
                    <td>${a.appointmentTime || 'N/A'}</td>
                    <td>${patName}</td>
                    <td>${docName}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-primary me-1" onclick="editAppointment(${JSON.stringify(a).replace(/"/g, '&quot;')})"><i class="fa-solid fa-pen"></i></button>
                        <button class="btn btn-sm btn-outline-danger" onclick="deleteAppointment(${a.id})"><i class="fa-solid fa-trash"></i></button>
                    </td>
                </tr>
            `;
        });
    } else {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted">No appointments found</td></tr>';
    }
}

async function editAppointment(a) {
    editingMode.appointments = true;
    document.getElementById('apptModalTitle').innerText = 'Update Appointment';
    document.getElementById('appt-id').value = a.id;
    document.getElementById('appt-id').readOnly = true;
    const patId = a.patient ? a.patient.id : a.patientId;
    const docId = a.doctor ? a.doctor.id : a.doctorId;
    await Promise.all([
        populatePatientDropdown('appt-pat-select', patId),
        populateDoctorDropdown('appt-doc-select', docId)
    ]);
    openModal('modalAppointment');
}

async function saveAppointment(e) {
    e.preventDefault();
    const payload = {
        id: parseInt(document.getElementById('appt-id').value),
        patientId: parseInt(document.getElementById('appt-pat-select').value),
        doctorId: parseInt(document.getElementById('appt-doc-select').value)
    };
    const method = editingMode.appointments ? 'PUT' : 'POST';
    const url = editingMode.appointments ? '/appointment/update' : '/appointment/save';

    try {
        await fetchAPI(url, { method, body: JSON.stringify(payload) });
        showAlert(editingMode.appointments ? 'Appointment updated successfully!' : 'Appointment booked successfully!');
        bootstrap.Modal.getInstance(document.getElementById('modalAppointment')).hide();
        loadAppointments();
    } catch (err) {}
}

async function deleteAppointment(id) {
    if (!confirm('Are you sure you want to delete this appointment?')) return;
    try {
        await fetchAPI(`/appointment/delete/${id}`, { method: 'DELETE' });
        showAlert('Appointment deleted successfully!');
        loadAppointments();
    } catch (e) {}
}

async function searchApptByDoctor() {
    const id = document.getElementById('appt-search-id').value.trim();
    if (!id) return loadAppointments();
    try {
        const res = await fetchAPI(`/appointment/viewByDoctor/${id}`);
        renderAppointments(res.data || []);
    } catch (e) {}
}

async function searchApptByPatient() {
    const id = document.getElementById('appt-search-id').value.trim();
    if (!id) return loadAppointments();
    try {
        const res = await fetchAPI(`/appointment/viewByPatient/${id}`);
        renderAppointments(res.data || []);
    } catch (e) {}
}

// ================= PRESCRIPTIONS =================
function openPrescriptionModal() {
    editingMode.prescriptions = false;
    document.getElementById('prescModalTitle').innerText = 'Add Prescription';
    document.getElementById('formPrescription').reset();
    document.getElementById('presc-id').readOnly = false;
    openModal('modalPrescription');
}

async function loadPrescriptions() {
    try {
        const res = await fetchAPI('/prescription/viewAll');
        renderPrescriptions(res.data || []);
    } catch (e) {}
}

async function searchPrescriptionById() {
    const id = document.getElementById('presc-search-id').value.trim();
    if (!id) return loadPrescriptions();
    try {
        const res = await fetchAPI(`/prescription/view/${id}`);
        renderPrescriptions(res.data ? [res.data] : []);
    } catch (e) {}
}

function renderPrescriptions(prescs) {
    const tbody = document.querySelector('#table-prescriptions tbody');
    tbody.innerHTML = '';
    if (prescs && prescs.length > 0) {
        prescs.forEach(p => {
            const apptId = p.appointmentId || (p.appointment ? p.appointment.id : 'N/A');
            tbody.innerHTML += `
                <tr>
                    <td>${p.id}</td>
                    <td><strong>${p.diagnosis}</strong></td>
                    <td>${p.medicines}</td>
                    <td>${p.instructions}</td>
                    <td>#${apptId}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-primary me-1" onclick="editPrescription(${JSON.stringify(p).replace(/"/g, '&quot;')})"><i class="fa-solid fa-pen"></i></button>
                        <button class="btn btn-sm btn-outline-danger" onclick="deletePrescription(${p.id})"><i class="fa-solid fa-trash"></i></button>
                    </td>
                </tr>
            `;
        });
    } else {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted">No prescriptions found</td></tr>';
    }
}

function editPrescription(p) {
    editingMode.prescriptions = true;
    document.getElementById('prescModalTitle').innerText = 'Update Prescription';
    document.getElementById('presc-id').value = p.id;
    document.getElementById('presc-id').readOnly = true;
    document.getElementById('presc-app-id').value = p.appointmentId || (p.appointment ? p.appointment.id : '');
    document.getElementById('presc-diagnosis').value = p.diagnosis;
    document.getElementById('presc-medicines').value = p.medicines;
    document.getElementById('presc-instructions').value = p.instructions;
    openModal('modalPrescription');
}

async function savePrescription(e) {
    e.preventDefault();
    const payload = {
        id: parseInt(document.getElementById('presc-id').value),
        appointmentId: parseInt(document.getElementById('presc-app-id').value),
        diagnosis: document.getElementById('presc-diagnosis').value,
        medicines: document.getElementById('presc-medicines').value,
        instructions: document.getElementById('presc-instructions').value
    };
    const method = editingMode.prescriptions ? 'PUT' : 'POST';
    const url = editingMode.prescriptions ? '/prescription/update' : '/prescription/save';

    try {
        await fetchAPI(url, { method, body: JSON.stringify(payload) });
        showAlert(editingMode.prescriptions ? 'Prescription updated successfully!' : 'Prescription added successfully!');
        bootstrap.Modal.getInstance(document.getElementById('modalPrescription')).hide();
        document.getElementById('formPrescription').reset();
        document.getElementById('presc-id').readOnly = false;
        editingMode.prescriptions = false;
        loadPrescriptions();
    } catch (err) {}
}

async function deletePrescription(id) {
    if (!confirm('Are you sure you want to delete this prescription?')) return;
    try {
        await fetchAPI(`/prescription/delete/${id}`, { method: 'DELETE' });
        showAlert('Prescription deleted successfully!');
        loadPrescriptions();
    } catch (e) {}
}

// ================= BILLING =================
async function openBillingModal() {
    editingMode.billings = false;
    document.getElementById('billModalTitle').innerText = 'Generate Bill';
    document.getElementById('formBilling').reset();
    document.getElementById('bill-id').readOnly = false;
    await populatePatientDropdown('bill-pat-select');
    openModal('modalBilling');
}

async function loadBillings() {
    try {
        const res = await fetchAPI('/billing/viewAll');
        renderBillings(res.data);
    } catch (e) {}
}

function renderBillings(bills) {
    const tbody = document.querySelector('#table-billings tbody');
    tbody.innerHTML = '';
    if (bills && bills.length > 0) {
        bills.forEach(b => {
            const patInfo = b.patient ? `${b.patient.name} (ID: ${b.patient.id})` : `Patient ID: ${b.patientId || 'N/A'}`;
            tbody.innerHTML += `
                <tr>
                    <td>${b.id}</td>
                    <td><strong class="text-success">$${b.amount}</strong></td>
                    <td><span class="badge bg-primary">${b.paymentMethod}</span></td>
                    <td>${patInfo}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-primary me-1" onclick="editBilling(${JSON.stringify(b).replace(/"/g, '&quot;')})"><i class="fa-solid fa-pen"></i></button>
                        <button class="btn btn-sm btn-outline-danger" onclick="deleteBilling(${b.id})"><i class="fa-solid fa-trash"></i></button>
                    </td>
                </tr>
            `;
        });
    } else {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted">No bill records found</td></tr>';
    }
}

async function editBilling(b) {
    editingMode.billings = true;
    document.getElementById('billModalTitle').innerText = 'Update Bill';
    document.getElementById('bill-id').value = b.id;
    document.getElementById('bill-id').readOnly = true;
    const patId = b.patient ? b.patient.id : b.patientId;
    await populatePatientDropdown('bill-pat-select', patId);
    document.getElementById('bill-amount').value = b.amount;
    document.getElementById('bill-payment-method').value = b.paymentMethod;
    openModal('modalBilling');
}

async function saveBilling(e) {
    e.preventDefault();
    const payload = {
        id: parseInt(document.getElementById('bill-id').value),
        patientId: parseInt(document.getElementById('bill-pat-select').value),
        amount: parseFloat(document.getElementById('bill-amount').value),
        paymentMethod: document.getElementById('bill-payment-method').value
    };
    const method = editingMode.billings ? 'PUT' : 'POST';
    const url = editingMode.billings ? '/billing/update' : '/billing/save';

    try {
        await fetchAPI(url, { method, body: JSON.stringify(payload) });
        showAlert(editingMode.billings ? 'Bill updated successfully!' : 'Bill generated successfully!');
        bootstrap.Modal.getInstance(document.getElementById('modalBilling')).hide();
        document.getElementById('formBilling').reset();
        document.getElementById('bill-id').readOnly = false;
        editingMode.billings = false;
        loadBillings();
    } catch (err) {}
}

async function deleteBilling(id) {
    if (!confirm('Are you sure you want to delete this bill record?')) return;
    try {
        await fetchAPI(`/billing/delete/${id}`, { method: 'DELETE' });
        showAlert('Bill deleted successfully!');
        loadBillings();
    } catch (e) {}
}

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    if (AUTH_HEADER) {
        showPortalView();
        loadDashboard();
    } else {
        showLoginView();
    }
});